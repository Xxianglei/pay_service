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

    @RequestMapping("/update")
    public BaseJson updateOrder(@RequestParam String flowId) {
        BaseJson baseJson = new BaseJson();
        int result = orderService.updateOrder(flowId);
        if (result != 0) {
            baseJson.setMessage("订单状态修改成功");
            baseJson.setStatus(true);
            baseJson.setCode(200);
        } else {
            baseJson.setMessage("订单状态修改失败");
            baseJson.setStatus(false);
            baseJson.setCode(500);
        }
        return baseJson;
    }
}
