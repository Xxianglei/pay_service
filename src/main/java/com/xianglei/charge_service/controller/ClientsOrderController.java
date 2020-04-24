package com.xianglei.charge_service.controller;

import com.xianglei.charge_service.common.BaseJson;
import com.xianglei.charge_service.common.utils.Tools;
import com.xianglei.charge_service.domain.BsOrder;
import com.xianglei.charge_service.domain.PreBsOrder;
import com.xianglei.charge_service.message.OrderProducer;
import com.xianglei.charge_service.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/14 13:55
 * com.xianglei.charge_service.controller
 * @Description: 客户端订单接口
 */
@RestController
@RequestMapping("/order")
public class ClientsOrderController {
    private Logger logger = LoggerFactory.getLogger(ClientsOrderController.class);
    @Autowired
    OrderService orderService;
    @Autowired
    OrderProducer orderProducer;

    /*
     * 客户端订单更新接口
     *更新订单只能修改关键数据   开始/结束/车牌号/白天还是晚上
     * @return
     */
    @RequestMapping("/updateOrder")
    public BaseJson UpdateOrder(@RequestBody BsOrder bsOrder) {
        BaseJson baseJson = new BaseJson(true);
        if (Tools.isNull(bsOrder)) {
            return new BaseJson(false, "数据错误");
        } else if (StringUtils.isEmpty(bsOrder.getUserId())) {
            logger.error("用户Id不可为空");
            return new BaseJson(false, "用户Id不可为空");
        } else {
            // 开始/结束/车牌号/白天还是晚上
            int num = orderService.updateOrder(bsOrder);
            if (num != 0) {
                baseJson.setMessage("更新成功");
                baseJson.setCode(200);
            } else {
                logger.error("用户更新订单失败");
                baseJson.setMessage("更新失败");
                baseJson.setCode(500);
                baseJson.setStatus(false);
            }
        }
        return baseJson;
    }

    /**
     * 客户端订单删除(批量)接口
     *
     * @return
     */
    @RequestMapping("/deleteOrder")
    public BaseJson DeleteOrder(@RequestParam List<String> flowIds) {
        BaseJson baseJson = new BaseJson(false);
        try {
            int nums = orderService.deleteOrders(flowIds);
            baseJson.setMessage("删除成功");
            baseJson.setCode(200);
            baseJson.setData(nums);
            baseJson.setStatus(true);
        } catch (Exception e) {
            baseJson.setMessage("删除出错");
        } finally {
            baseJson.setMessage("删除出错");
        }
        return baseJson;
    }

    /**
     * 客户端查看自己的订单（根据id和订单状态）接口
     * 支付状态/0，1，2/未支付/已支付/已过期
     *
     * @return
     */
    @RequestMapping("/viewOrder")
    public BaseJson ViewOrder(@RequestParam String userId, String chargeStatus) {
        BaseJson baseJson = new BaseJson(true);
        if (StringUtils.isEmpty(userId)) {
            logger.error("用户Id不可为空");
            return new BaseJson(false, "用户Id不可为空");
        } else {
            List<PreBsOrder>  myOrders = orderService.getMyOrders(userId, chargeStatus);
            baseJson.setData(myOrders);
            baseJson.setCode(200);
            baseJson.setMessage("查询成功");
        }
        return baseJson;
    }
}
