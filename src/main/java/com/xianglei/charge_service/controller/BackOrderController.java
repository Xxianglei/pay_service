package com.xianglei.charge_service.controller;

import com.xianglei.charge_service.common.BaseJson;
import com.xianglei.charge_service.domain.PreOrder;
import com.xianglei.charge_service.service.ParkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 21:41
 * com.xianglei.charge_service.controller
 * @Description: 后台订单管理Controller
 */
@RestController
@RequestMapping("/back")
public class BackOrderController {
    private Logger logger = LoggerFactory.getLogger(BackOrderController.class);
    @Autowired
    ParkService parkService;

    /**
     * 管理员查看所有订单
     *
     * @param parkName
     * @param
     * @return
     */
    @RequestMapping("/viewOrder")
    public BaseJson ViewOrder(@RequestParam("parkName") String parkName) {
        BaseJson baseJson = new BaseJson(true);
        List<PreOrder> park = parkService.getPark(parkName);
        baseJson.setData(park);
        baseJson.setCode(200);
        baseJson.setMessage("查询成功");
        return baseJson;
    }
}
