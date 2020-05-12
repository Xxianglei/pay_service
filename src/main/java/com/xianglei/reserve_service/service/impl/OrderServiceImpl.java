package com.xianglei.reserve_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xianglei.reserve_service.common.DateEnum;
import com.xianglei.reserve_service.common.OrderStatusEnum;
import com.xianglei.reserve_service.common.utils.DateUtils;
import com.xianglei.reserve_service.common.utils.RedisUtil;
import com.xianglei.reserve_service.common.utils.Tools;
import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.domain.BsPark;
import com.xianglei.reserve_service.domain.BsParkInfo;
import com.xianglei.reserve_service.domain.PreBsOrder;
import com.xianglei.reserve_service.mapper.OrderMapper;
import com.xianglei.reserve_service.mapper.ParkInfoMapper;
import com.xianglei.reserve_service.mapper.ParkMapper;
import com.xianglei.reserve_service.message.OrderProducer;
import com.xianglei.reserve_service.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 13:56
 * com.xianglei.reserve_service.service.impl
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ParkInfoMapper parkInfoMapper;
    @Autowired
    ParkMapper parkMapper;
    @Autowired
    OrderProducer orderProducer;
    @Autowired
    RedisUtil redisUtil;

    @Transactional
    @Override
    public int deleteOrders(List<String> flowIds, String userId) {
        // 不可批量删除
        BsOrder bsOrder = orderMapper.selectById(flowIds.get(0));
        int nums = orderMapper.deleteBatchIds(flowIds);
        // 找到对应车位，释放车位  这个flowId是订单id
        String parkInfoId = bsOrder.getParkInfoId();
        String parkId = bsOrder.getParkId();
        BsParkInfo bsParkInfo = parkInfoMapper.selectOne(new QueryWrapper<BsParkInfo>()
                .eq("PARK_NUM", parkInfoId)
                .eq("PARK_ID", parkId));
        if(Tools.isNotNull(bsParkInfo)){
            releaseParkInfo(bsParkInfo.getFlowId(), userId);
        }
        return nums;
    }

    @Transactional
    @Override
    public List<PreBsOrder> getMyOrders(String userId, String orderId) {
        QueryWrapper<BsOrder> bsOrderQueryWrapper = new QueryWrapper<>();
        bsOrderQueryWrapper.eq("USER_ID", userId);
        bsOrderQueryWrapper.orderByDesc("CREATE_TIME");
        if (!StringUtils.isEmpty(orderId)) {
            bsOrderQueryWrapper.eq("FLOW_ID", orderId);
        }
        List<PreBsOrder> list = new ArrayList<>();
        List<BsOrder> bsOrders = orderMapper.selectList(bsOrderQueryWrapper);
        for (BsOrder bsOrder : bsOrders) {
            PreBsOrder preBsOrder = new PreBsOrder();
            if ("0".equals(bsOrder.getCharge())) {
                bsOrder.setCharge(OrderStatusEnum.NO_PAY.getName());
                // 如果订单未支付的时候 从redis拿到订单的剩余存活时间
      /*          long expire = redisUtil.getExpire(bsOrder.getFlowId());
                // 设置过期时间
                preBsOrder.setExpireTime(expire/1000);*/
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
            // 时间格式化
            if (bsOrder.getStartTime() != null && bsOrder.getLeaveTime() != null && bsOrder.getCreateTime() != null) {
                String limitStart = "06:00:00";
                String limitEnd = "18:00:00";
                String startTime = DateUtils.format(bsOrder.getStartTime(), "yyyy-MM-dd HH:mm:ss");
                preBsOrder.setStartTime(startTime);
                String endTime = DateUtils.format(bsOrder.getLeaveTime(), "yyyy-MM-dd HH:mm:ss");
                preBsOrder.setLeaveTime(endTime);
                String createTime = DateUtils.format(bsOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
                preBsOrder.setCreateTime(createTime);
                if (startTime.split(" ")[1].compareTo(limitStart) >= 0 & endTime.split(" ")[1].compareTo(limitEnd) <= 0) {
                    // 按白天计价
                    preBsOrder.setEvening("0");
                } else {
                    // 按夜间计价
                    preBsOrder.setEvening("1");
                }
            }
            String parkId = bsOrder.getParkId();
            // 获取停车场对象
            BsPark bsPark = parkMapper.selectById(parkId);
            // 获取停车场名字
            String parkName = bsPark.getParkName();
            preBsOrder.setParkName(parkName);
            // 获取停车场位置
            String location = bsPark.getLocation();
            preBsOrder.setLocation(location);
            preBsOrder.setPrice(bsOrder.getPrice());
            preBsOrder.setFlowId(bsOrder.getFlowId());
            preBsOrder.setUserId(userId);
            preBsOrder.setCarNum(bsOrder.getCarNum());
            preBsOrder.setParkInfoId(bsOrder.getParkInfoId());
            preBsOrder.setParkId(bsOrder.getParkId());
            preBsOrder.setCharge(bsOrder.getCharge());
            list.add(preBsOrder);
        }
        return list;
    }

    @Transactional
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
        if (bsOrder.getPrice() != null) {
            myOrder.setPrice(bsOrder.getPrice());
        }
        int i = orderMapper.updateById(myOrder);
        //更新订单信息
        return i;
    }

    @Transactional
    @Override
    public int updateOrder(String flowId) {
        // 查到原来的订单信息
        BsOrder myOrder = orderMapper.selectById(flowId);
        if (Tools.isNotNull(myOrder)) {
            myOrder.setCharge("1");
        }
        return orderMapper.updateById(myOrder);
    }

    /**
     * 乐观锁解决并发问题
     *
     * @param bsOrderMap
     * @return
     */
    @Transactional
    @Override
    public int updateParkStatus(Map<String, String> bsOrderMap) {
        BsOrder bsOrder = new BsOrder();
        bsOrder.setCarNum(bsOrderMap.get("carNum"));
        String leaveTime = bsOrderMap.get("leaveTime");
        String startTime = bsOrderMap.get("startTime");
        // 获取当前日期  拼接时间  转换为Date存入数据库
        startTime = startTime + ":00";
        leaveTime = leaveTime + ":00";
        bsOrder.setLeaveTime(DateUtils.parse(leaveTime, "yyyy-MM-dd HH:mm:ss"));
        bsOrder.setStartTime(DateUtils.parse(startTime, "yyyy-MM-dd HH:mm:ss"));
        bsOrder.setParkId(bsOrderMap.get("parkId"));
        bsOrder.setUserId(bsOrderMap.get("userId"));
        bsOrder.setParkInfoId(bsOrderMap.get("parkInfoId"));
        bsOrder.setFlowId(UUID.randomUUID().toString());
        if (leaveTime.compareTo("18:00") <= 0 && startTime.compareTo("06:00") >= 0) {
            bsOrder.setEvening("0");
        } else {
            bsOrder.setEvening("1");
        }
        String parkId = bsOrder.getParkId();
        String userId = bsOrder.getUserId();
        String parkInfoId = bsOrder.getParkInfoId();
        BsParkInfo parkInfo = parkInfoMapper.selectOne(new QueryWrapper<BsParkInfo>().eq("PARK_ID", parkId).eq("PARK_NUM", parkInfoId));
        String tempOwner = parkInfo.getTempOwner();
        int num = 0;
        if (StringUtils.isEmpty(tempOwner)) {
            // 如果车位任何时段都没有人占用 直接更新
            parkInfo.setTempOwner(userId + "@");
            int index = 3;
            // 三次自旋
            while (index > 0) {
                if (num != 0) {
                    break;
                } else {
                    num = parkInfoMapper.update(parkInfo, new UpdateWrapper<BsParkInfo>()
                            .eq("PARK_ID", parkInfo.getParkId())
                            .eq("PARK_NUM", parkInfoId));
                    index--;
                }
            }
            if (num != 0) {
                SendResult sendResult = orderProducer.sendOrder(bsOrder);
                SendStatus sendStatus = sendResult.getSendStatus();
                if (sendStatus.ordinal() != 0) {
                    throw new RuntimeException("消息发送失败");
                }
            }
        } else {
            // 如果有人占用则拼接
            StringBuffer stringBuffer = new StringBuffer(tempOwner);
            stringBuffer.append(userId);
            stringBuffer.append("@");
            parkInfo.setTempOwner(stringBuffer.toString());
            int index = 3;
            // 三次自旋
            while (index > 0) {
                if (num != 0) {
                    break;
                } else {
                    num = parkInfoMapper.update(parkInfo, new UpdateWrapper<BsParkInfo>()
                            .eq("TEMP_OWNER", tempOwner)
                            .eq("PARK_ID", parkInfo.getParkId())
                            .eq("PARK_NUM", parkInfoId));
                    index--;
                }
            }
            // 后发消息
            if (num != 0) {
                SendResult sendResult = orderProducer.sendOrder(bsOrder);
                SendStatus sendStatus = sendResult.getSendStatus();
                if (sendStatus.ordinal() != 0) {
                    throw new RuntimeException("消息发送失败");
                }
            }

        }
        return num;
    }

    @Override
    public int insertOrder(BsOrder bsOrder) {
        int insert = orderMapper.insert(bsOrder);
        return insert;
    }

    @Override
    public int releaseParkInfo(String flowId, String userId) {
        QueryWrapper<BsParkInfo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("FLOW_ID", flowId);
        BsParkInfo parkInfo = parkInfoMapper.selectOne(objectQueryWrapper);
        // 当前临时拥有者设置为空
        String tempOwner = parkInfo.getTempOwner();
        if (StringUtils.isNotEmpty(tempOwner)) {
            String replace = tempOwner.replace(userId + "@", "");
            parkInfo.setTempOwner(replace);
        }
        return parkInfoMapper.updateById(parkInfo);
    }

    @Override
    public int checkOrderIsOk(Map<String, String> bsOrder) {
        String startTime = bsOrder.get("startTime");
        String leaveTime = bsOrder.get("leaveTime");
        startTime =  startTime + ":00";
        leaveTime =  leaveTime + ":00";
        List<BsOrder> bsOrders = orderMapper.selectList(new QueryWrapper<BsOrder>()
                .eq("USER_ID", bsOrder.get("userId"))
                .eq("PARK_INFO_ID", bsOrder.get("parkInfoId"))
                .eq("PARK_ID", bsOrder.get("parkId"))
                .eq("START_TIME", startTime)
                .eq("LEAVE_TIME", leaveTime));
        if (bsOrders != null && bsOrders.size() != 0) {
            return 1;
        }
        return 0;
    }

}
