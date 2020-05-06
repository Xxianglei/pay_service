package com.xianglei.reserve_service.service.feigncall;

import com.xianglei.reserve_service.common.BaseJson;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 21:21
 * com.xianglei.reserve_service.service.feigncall
 * @Description:远程调用计费策略
 */
@FeignClient(value = "account-service")
public interface AccountStrategy {
    @GetMapping("/getPrice")
    BaseJson getPriceByOrder(@RequestParam String orderId);
}
