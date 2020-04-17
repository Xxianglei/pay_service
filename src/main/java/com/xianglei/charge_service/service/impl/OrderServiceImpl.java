package com.xianglei.charge_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xianglei.charge_service.common.DateEnum;
import com.xianglei.charge_service.common.OrderStatusEnum;
import com.xianglei.charge_service.common.utils.Tools;
import com.xianglei.charge_service.domain.BsOrder;
import com.xianglei.charge_service.domain.BsParkInfo;
import com.xianglei.charge_service.mapper.OrderMapper;
import com.xianglei.charge_service.mapper.ParkInfoMapper;
import com.xianglei.charge_service.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 13:56
 * com.xianglei.charge_service.service.impl
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ParkInfoMapper parkInfoMapper;

    @Override
    public int deleteOrders(List<String> flowIds) {
        int nums = orderMapper.deleteBatchIds(flowIds);
        // 找到对应车位，释放车位
        for (String flowId : flowIds) {
            releaseParkInfo(flowId);
        }
        return nums;
    }


    @Override
    public List<BsOrder> getMyOrders(String userId, String status) {
        QueryWrapper<BsOrder> bsOrderQueryWrapper = new QueryWrapper<>();
        bsOrderQueryWrapper.eq("USER_ID", userId);
        if (!StringUtils.isEmpty(status)) {
            bsOrderQueryWrapper.eq("CHARGE", userId);
        }
        List<BsOrder> bsOrders = orderMapper.selectList(bsOrderQueryWrapper);
        for (BsOrder bsOrder : bsOrders) {
            if ("0".equals(bsOrder.getCharge())) {
                bsOrder.setCharge(OrderStatusEnum.NO_PAY.getName());
            } else if ("1".equals(bsOrder.getCharge())) {
                bsOrder.setCharge(OrderStatusEnum.PAYED.getName());
            } else {
                bsOrder.setCharge(OrderStatusEnum.EXPIRE.getName());
            }
            if ("0".equals(bsOrder.getEvening())) {
                bsOrder.setEvening(DateEnum.DAY.getName());
            } else {
                bsOrder.setEvening(DateEnum.NIGHT.getName());
            }
        }
        return bsOrders;
    }

    @Override
    public int updateOrder(BsOrder bsOrder) {
        // 查到原来的订单信息
        BsOrder myOrder = orderMapper.selectById(bsOrder.getFlowId());
        if (StringUtils.isNotEmpty(bsOrder.getEvening())) {
            myOrder.setEvening(bsOrder.getEvening());
        }
        if (bsOrder.getStartTime() != null) {
            myOrder.setStartTime(bsOrder.getStartTime());
        }
        if (bsOrder.getLeaveTime() != null) {
            myOrder.setLeaveTime(bsOrder.getLeaveTime());
        }
        if (StringUtils.isNotEmpty(bsOrder.getCarNum())) {
            myOrder.setCarNum(bsOrder.getCarNum());
        }
        int i = orderMapper.updateById(myOrder);
        //更新订单信息
        return i;
    }

    @Override
    public int updateOrder(String flowId) {
        // 查到原来的订单信息
        BsOrder myOrder = orderMapper.selectById(flowId);
        if (Tools.isNotNull(myOrder)) {
            myOrder.setCharge("1");
        }
        return orderMapper.updateById(myOrder);
    }

    @Override
    public int updateParkStatus(String flowId, String userId) {
        BsParkInfo parkInfo = parkInfoMapper.selectById(flowId);
        int num = 0;
        if (Tools.isNotNull(parkInfo)) {
            parkInfo.setStatus("0");
            parkInfo.setTempOwner(userId);
            num = parkInfoMapper.update(parkInfo, new UpdateWrapper<BsParkInfo>().ne("STATUS", 0));
        }
        return num;
    }

    @Override
    public int insertOrder(BsOrder bsOrder) {
        int insert = orderMapper.insert(bsOrder);
        return insert;
    }

    @Override
    public int releaseParkInfo(String flowId) {
        QueryWrapper<BsParkInfo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("FLOW_ID", flowId);
        BsParkInfo parkInfo = parkInfoMapper.selectOne(objectQueryWrapper);
        parkInfo.setStatus("2");
        // 临时拥有者设置为空
        parkInfo.setTempOwner("");
        return parkInfoMapper.updateById(parkInfo);
    }

}
