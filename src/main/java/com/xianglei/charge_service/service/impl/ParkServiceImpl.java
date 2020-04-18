package com.xianglei.charge_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xianglei.charge_service.domain.*;
import com.xianglei.charge_service.mapper.CarMapper;
import com.xianglei.charge_service.mapper.OrderMapper;
import com.xianglei.charge_service.mapper.ParkMapper;
import com.xianglei.charge_service.mapper.UserMapper;
import com.xianglei.charge_service.service.ParkService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 21:47
 * com.xianglei.charge_service.service.impl
 * @Description:
 */
@Service
public class ParkServiceImpl implements ParkService {
    @Autowired
    ParkMapper parkMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    CarMapper carMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<PreOrder> getPark(String name) {
        ArrayList<PreOrder> preOrders = new ArrayList<>();
        if (StringUtils.isNotEmpty(name)) {
            //查询特有的订单 按创建时间倒叙
            BsPark bsPark = parkMapper.selectOne(new QueryWrapper<BsPark>().eq("PARK_NAME", name));
            // 停车场id
            String flowId = bsPark.getFlowId();
            // 查到当前停车场的所有订单
            List<BsOrder> bsOrders = orderMapper.selectList(new QueryWrapper<BsOrder>().eq("PARK_ID", flowId));
            for (BsOrder bsOrder : bsOrders) {
                PreOrder preOrder = new PreOrder();
                preOrder.setParkName(name);
                BsUser bsUser = userMapper.selectOne(new QueryWrapper<BsUser>().eq("FLOW_ID", bsOrder.getUserId()));
                preOrder.setName(bsUser.getName());
                preOrder.setCreateDate(bsOrder.getCreateTime());
                preOrder.setEndDate(bsOrder.getLeaveTime());
                preOrder.setStartDate(bsOrder.getStartTime());
                preOrder.setParKNo(bsOrder.getParkInfoId());
                preOrder.setCarNum(bsOrder.getCarNum());
                // 获取车辆色号
                BsUserCar bsUserCar = carMapper.selectOne(new QueryWrapper<BsUserCar>().eq("USER_ID", bsOrder.getUserId()).eq("CAR_NUM", bsOrder.getCarNum()));
                preOrder.setColor(bsUserCar.getColor());
                preOrders.add(preOrder);
            }
        } else {
            //查询所有的订单 按创建时间倒叙
            List<BsOrder> bsOrders = orderMapper.selectList(null);
            for (BsOrder bsOrder : bsOrders) {
                // 当前订单的停车场
                String parkId = bsOrder.getParkId();
                PreOrder preOrder = new PreOrder();
                BsPark bsPark = parkMapper.selectOne(new QueryWrapper<BsPark>().eq("FLOW_ID", parkId));
                preOrder.setParkName(bsPark.getParkName());
                // 下单人名字
                BsUser bsUser = userMapper.selectOne(new QueryWrapper<BsUser>().eq("FLOW_ID", bsOrder.getUserId()));
                preOrder.setName(bsUser.getName());
                preOrder.setCreateDate(bsOrder.getCreateTime());
                preOrder.setStartDate(bsOrder.getStartTime());
                preOrder.setEndDate(bsOrder.getLeaveTime());
                preOrder.setParKNo(bsOrder.getParkInfoId());
                preOrder.setCarNum(bsOrder.getCarNum());
                // 获取车辆色号
                BsUserCar bsUserCar = carMapper.selectOne(new QueryWrapper<BsUserCar>().eq("USER_ID", bsOrder.getUserId()).eq("CAR_NUM", bsOrder.getCarNum()));
                preOrder.setColor(bsUserCar.getColor());
                preOrders.add(preOrder);
            }
        }
        /*
        按创建时间排序
         */
        preOrders.sort(new Comparator<PreOrder>() {
            @Override
            public int compare(PreOrder o1, PreOrder o2) {
                return o2.getCreateDate().compareTo(o1.getCreateDate());
            }
        });
        return preOrders;
    }
}
