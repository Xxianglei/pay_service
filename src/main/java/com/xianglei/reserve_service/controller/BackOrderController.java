package com.xianglei.reserve_service.controller;

import com.xianglei.reserve_service.common.BaseJson;
import com.xianglei.reserve_service.domain.PreOrder;
import com.xianglei.reserve_service.service.ParkService;
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
 * com.xianglei.reserve_service.controller
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
    /**
     * 管理员查看所有订单
     *
     * @param flowId
     * @param
     * @return
     */
    @RequestMapping("/deleteOrder")
    public BaseJson deleteOrder(@RequestParam("flowId") String flowId) {
        BaseJson baseJson = new BaseJson(true);
        int res = parkService.deleteOrder(flowId);
        if(res!=0){
            baseJson.setCode(200);
            baseJson.setMessage("查询成功");
        }else{

        }    baseJson.setCode(500);
        baseJson.setMessage("查询失败");
        return baseJson;
    }
}
