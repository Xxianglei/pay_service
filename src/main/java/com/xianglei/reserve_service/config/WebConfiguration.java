package com.xianglei.reserve_service.config;

import com.xianglei.reserve_service.interceptor.OrderQueueInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/5/9 10:06
 * com.xianglei.reserve_service.config
 * @Description:
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Resource
    private OrderQueueInterceptor queueInterceptor;

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(queueInterceptor);
        interceptorRegistration.addPathPatterns("/subOrder/**");
    }
}
