package com.xianglei.reserve_service.controller;

import com.xianglei.reserve_service.common.BaseJson;
import com.xianglei.reserve_service.common.utils.RedisUtil;
import com.xianglei.reserve_service.common.utils.Tools;
import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.message.OrderProducer;
import com.xianglei.reserve_service.service.OrderService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 13:47
 * com.xianglei.reserve_service.controller
 * @Description: 订单预约(秒杀)接口
 */
@RestController
@RequestMapping("/subOrder")
public class SubscribeOrderController {
    private Logger logger = LoggerFactory.getLogger(SubscribeOrderController.class);
    @Autowired
    OrderProducer orderProducer;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisUtil redisUtil;


    /**
     * 客户端下单第一步占用车位
     *
     * @return
     */
    @RequestMapping("/lockOrder")
    public BaseJson PlaceOrder(@RequestParam String flowId, @RequestParam String userId) {
        BaseJson baseJson = new BaseJson();
        int num = orderService.updateParkStatus(flowId, userId);
        if (num != 0) {
            baseJson.setStatus(true);
            baseJson.setCode(200);
            baseJson.setMessage("锁单成功");
        } else {
            baseJson.setStatus(false);
            baseJson.setCode(500);
            baseJson.setMessage("锁单失败");
        }
        return baseJson;
    }

    /**
     * 生成订单,保存本地同时，发送到消息队列
     *
     * @param bsOrder
     * @return
     */
    @RequestMapping("/placeOrder")
    public BaseJson placeOrder(@RequestBody BsOrder bsOrder) {
        BaseJson baseJson = new BaseJson();
        if (Tools.isNotNull(bsOrder)) {
            baseJson.setMessage("参数错误");
            baseJson.setCode(500);
            baseJson.setStatus(false);
        } else {
            SendResult sendResult = orderProducer.sendOrder(bsOrder);
            SendStatus sendStatus = sendResult.getSendStatus();
            // 如果是第0个枚举
            if (sendStatus.ordinal() == 0) {
                baseJson.setMessage("下单成功");
                baseJson.setCode(200);
                baseJson.setStatus(true);
            } else {
                baseJson.setMessage("下单失败");
                baseJson.setCode(500);
                baseJson.setStatus(false);
            }
        }
        return baseJson;
    }

    /**
     * 支付订单
     *
     * @param bsOrder
     * @return
     */
    @RequestMapping("/payOrder")
    public BaseJson payOrder(@RequestBody BsOrder bsOrder) {
        BaseJson baseJson = new BaseJson();

        return baseJson;
    }
}
