package com.xianglei.reserve_service.interceptor;

import cn.hutool.json.JSONUtil;
import com.xianglei.reserve_service.common.http.RequestWrapper;
import com.xianglei.reserve_service.common.utils.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/5/9 10:07
 * com.xianglei.reserve_service.interceptor
 * @Description:
 */
@Component
public class OrderQueueInterceptor implements HandlerInterceptor {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedisTemplate redisTemplate;
    public static final String ORDER_QUEUE_KEY = "orderQueue";
    public static final Long EXPIRE_TIME = 30 * 60L;

    /**
     * 拦截锁单请求放入redis list 排队
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestWrapper requestWrapper = new RequestWrapper(request);
        // 获取@RequestBody注解参数和post请求参数
        String body = requestWrapper.getBody();
        if (StringUtils.isNotEmpty(body)) {
            HashMap hashMap = JSONUtil.toBean(body, HashMap.class);
            boolean goTag = redisUtil.lSet(ORDER_QUEUE_KEY, hashMap.get("userId"));
            if (goTag) {
                // 放行
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
