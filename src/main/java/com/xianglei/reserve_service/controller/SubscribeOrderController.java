package com.xianglei.reserve_service.controller;

import com.xianglei.reserve_service.common.BaseJson;
import com.xianglei.reserve_service.common.utils.RedisUtil;
import com.xianglei.reserve_service.common.utils.Tools;
import com.xianglei.reserve_service.message.OrderProducer;
import com.xianglei.reserve_service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
     * 锁单成功后立马执行生成订单 发送消息队列
     *
     * @return
     */
    @RequestMapping("/lockOrder")
    public BaseJson PlaceOrder(@RequestBody Map<String, String> bsOrderMap) {
        BaseJson baseJson = new BaseJson();
        if (Tools.isNull(bsOrderMap)) {
            baseJson.setMessage("参数错误");
            baseJson.setCode(500);
            baseJson.setStatus(false);
        } else {
            int num = orderService.updateParkStatus(bsOrderMap);
            if (num != 0) {
                if (num != 0) {
                    // 二次校验订单是否存在
                    int res = orderService.checkOrderIsOk(bsOrderMap);
                    if (res != 0) {
                        logger.error("二次校验用户更新订单失败");
                        baseJson.setMessage("重复锁单失败");
                        baseJson.setCode(500);
                        baseJson.setStatus(false);
                    } else {
                        baseJson.setStatus(true);
                        baseJson.setCode(200);
                        baseJson.setMessage("锁单成功");
                    }
                } else {
                    baseJson.setStatus(false);
                    baseJson.setCode(500);
                    baseJson.setMessage("锁单失败");
                }
            }
        }
        return baseJson;
    }

    /**
     * 扫描二维码临时停车
     *
     * @param bsOrderMap 车号  停车场id  用户id
     * @return
     */
    @RequestMapping("/tempPark")
    public BaseJson tempPark(@RequestBody Map<String, String> bsOrderMap) {
        BaseJson baseJson = new BaseJson();
        if (Tools.isNull(bsOrderMap)) {
            baseJson.setMessage("参数错误");
            baseJson.setCode(500);
            baseJson.setStatus(false);
        } else {
            // 判断当前停车场是否有临时订单
            if (orderService.existTempOrder(bsOrderMap) != 0) {
                // 如果有临时订单  走支付  更新订单
                int result = orderService.updateOrder(bsOrderMap);
                if (result == 0) {
                    logger.error("离开停车场支付失败");
                    baseJson.setMessage("支付失败");
                    baseJson.setCode(500);
                    baseJson.setStatus(false);
                } else {
                    baseJson.setStatus(true);
                    baseJson.setCode(200);
                    baseJson.setMessage("支付成功");
                }
            }else{
                int result = orderService.generateTempOrder(bsOrderMap);
                if (result == 0) {
                    logger.error("临时停车下订单失败");
                    baseJson.setMessage("下单失败");
                    baseJson.setCode(500);
                    baseJson.setStatus(false);
                } else {
                    baseJson.setStatus(true);
                    baseJson.setCode(200);
                    baseJson.setMessage("下单成功");
                }
            }
        }
        return baseJson;
    }
}
