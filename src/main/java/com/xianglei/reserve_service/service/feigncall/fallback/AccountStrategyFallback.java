package com.xianglei.reserve_service.service.feigncall.fallback;

import com.xianglei.reserve_service.common.BaseJson;
import com.xianglei.reserve_service.service.feigncall.AccountStrategy;
import org.springframework.stereotype.Component;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/5/6 13:44
 * com.xianglei.reserve_service.service.feigncall.fallback
 * @Description:
 */
@Component
public class AccountStrategyFallback implements AccountStrategy {
    @Override
    public BaseJson getPriceByOrder(String orderId) {
        return new BaseJson(false,"服务熔断");
    }
}
