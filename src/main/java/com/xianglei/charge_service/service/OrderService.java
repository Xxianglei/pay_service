package com.xianglei.charge_service.service;

import com.xianglei.charge_service.domain.BsOrder;

import java.util.List;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 13:55
 * com.xianglei.charge_service.service
 * @Description:订单服务
 */
public interface OrderService {

    int deleteOrders(List<String> flows);

    List<BsOrder> getMyOrders(String userId, String status);

    int updateOrder(BsOrder bsOrder);

    int updateOrder(String flowId);

    int updateParkStatus(String flowId, String userId);

    int insertOrder(BsOrder bsOrder);

    int releaseParkInfo(String flowId);
}
