package com.xianglei.reserve_service.executor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xianglei.reserve_service.common.BaseJson;
import com.xianglei.reserve_service.common.utils.Tools;
import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.domain.BsParkInfo;
import com.xianglei.reserve_service.mapper.OrderMapper;
import com.xianglei.reserve_service.mapper.ParkInfoMapper;
import com.xianglei.reserve_service.service.OrderService;
import com.xianglei.reserve_service.service.feigncall.AccountStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 18:47
 * com.xianglei.reserve_service.executor
 * @Description:线程方法批量插入数据库
 */
@Component
public class RedisConsumerCallable implements Callable {

    private Logger logger = LoggerFactory.getLogger(RedisConsumerCallable.class);
    @Autowired
    OrderService orderService;
    @Autowired
    AccountStrategy accountStrategy;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ParkInfoMapper parkInfoMapper;
    BsOrder bsOrder;

    public void setOrder(BsOrder bsOrder) {
        this.bsOrder = bsOrder;
    }

    @Override
    public Object call() throws Exception {
        synchronized (RedisConsumerCallable.class) {
            int insertOrder = 0;
            // 获取userID判断是否是数组第一个如果是则开始下单，如果不是则判断是否已经已经被其他用户占用了当前时间段，如果没有则新增订单
            BsParkInfo parkInfo = parkInfoMapper.selectOne(new QueryWrapper<BsParkInfo>().eq("PARK_ID", bsOrder.getParkId()).eq("PARK_NUM", bsOrder.getParkInfoId()));
            // 不可能是空
            String tempOwner = parkInfo.getTempOwner();
            int index = tempOwner.indexOf(bsOrder.getUserId());
            // 如果能成功必定是第0个
            if (index != 0) {
                // 判断是否其他用户占用同时间段(大包小)
                Date startTime = bsOrder.getStartTime();
                Date leaveTime = bsOrder.getLeaveTime();
                List<BsOrder> bsOrders = orderMapper.selectList(new QueryWrapper<BsOrder>()
                        .ge("START_TIME", startTime).le("LEAVE_TIME", leaveTime)
                        .eq("PARK_ID", bsOrder.getParkId())
                        .eq("PARK_NUM", bsOrder.getParkInfoId()));
                if (Tools.isNotEmpty(bsOrders)) {
                    insertOrder = 0;
                } else {
                    insertOrder = orderService.insertOrder(bsOrder);
                    // 设置订单的价位
                    BaseJson priceByOrder = accountStrategy.getPriceByOrder(bsOrder.getFlowId());
                    if (priceByOrder.isStatus() && priceByOrder.getData() != null) {
                        Double data = (Double) priceByOrder.getData();
                        bsOrder.setPrice(data);
                        orderService.updateOrder(bsOrder);
                    } else {
                        logger.error("远程调用计算价格失败");
                        throw new RuntimeException("远程调用计算价格失败");
                    }
                }
            } else {
                insertOrder = orderService.insertOrder(bsOrder);
                // 设置订单的价位
                BaseJson priceByOrder = accountStrategy.getPriceByOrder(bsOrder.getFlowId());
                if (priceByOrder.isStatus() && priceByOrder.getData() != null) {
                    Double data = (Double) priceByOrder.getData();
                    bsOrder.setPrice(data);
                    orderService.updateOrder(bsOrder);
                } else {
                    logger.error("远程调用计算价格失败");
                    throw new RuntimeException("远程调用计算价格失败");
                }
            }
            return insertOrder;
        }
    }
}
