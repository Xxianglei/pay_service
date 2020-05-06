package com.xianglei.reserve_service.controller;

import com.xianglei.reserve_service.common.BaseJson;
import com.xianglei.reserve_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Xianglei
 * @Description //TODO
 * @Date 2020/4/17
 * @Param 支付PayController
 **/
@RestController
@RequestMapping("/pay")
public class PayController {
    @Autowired
    OrderService orderService;

    /**
     * 远程调用  根据策略计算价格
     *
     * @param flowId
     * @return
     */
    @RequestMapping("/price")
    public BaseJson sendPrice(@RequestParam String flowId) {
        // TODO 调用计价策略
        BaseJson baseJson = new BaseJson();
        return baseJson;
    }
}
