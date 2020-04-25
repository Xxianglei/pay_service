package com.xianglei.reserve_service.service;

import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.domain.PreBsOrder;

import java.util.List;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 13:55
 * com.xianglei.reserve_service.service
 * @Description:订单服务
 */
public interface OrderService {

    int deleteOrders(List<String> flows);

    List<PreBsOrder>  getMyOrders(String userId, String orderId);

    int updateOrder(BsOrder bsOrder);

    int updateOrder(String flowId);

    int updateParkStatus(String flowId, String userId);

    int insertOrder(BsOrder bsOrder);

    int releaseParkInfo(String flowId);
}
