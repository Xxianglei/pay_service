package com.xianglei.reserve_service.service;

import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.domain.PreBsOrder;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 13:55
 * com.xianglei.reserve_service.service
 * @Description:订单服务
 */
public interface OrderService {

    int deleteOrders(List<String> flows,String userId);

    List<PreBsOrder>  getMyOrders(String userId, String orderId);

    int updateOrder(BsOrder bsOrder);

    int updateOrder(String flowId);

    int updateParkStatus(Map<String, String> bsOrder);

    int insertOrder(BsOrder bsOrder);

    int releaseParkInfo(String flowId, String userId);

    /**
     * 二次校验
     * @param bsOrder
     * @return
     */
    int checkOrderIsOk(Map<String, String> bsOrder);
}
