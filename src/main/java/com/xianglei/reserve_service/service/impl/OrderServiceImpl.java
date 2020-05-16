package com.xianglei.reserve_service.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xianglei.reserve_service.common.BaseJson;
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
import com.xianglei.reserve_service.service.feigncall.AccountStrategy;
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
    @Autowired
    AccountStrategy accountStrategy;

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
        if (Tools.isNotNull(bsParkInfo)) {
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
        startTime = startTime + ":00";
        leaveTime = leaveTime + ":00";
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

    @Override
    public int generateTempOrder(Map<String, String> bsOrderMap) {
        BsOrder bsOrder = new BsOrder();
        String parkId = bsOrderMap.get("parkId");
        String carNum = bsOrderMap.get("carNum");
        String userId = bsOrderMap.get("userId");
        // 当前时间
        Date createDate = new Date();
        String formatCreateDate = DateUtil.format(createDate, "yyyy-MM-dd HH:mm:ss");
        // 默认00点离开
        String leaveDate = DateUtil.format(createDate, "yyyy-MM-dd") + " 23:30:00";
        bsOrder.setFlowId(UUID.randomUUID().toString() + "TEMP");
        bsOrder.setParkId(parkId);
        bsOrder.setCarNum(carNum);
        bsOrder.setLeaveTime(DateUtil.parse(leaveDate, "yyyy-MM-dd HH:mm:ss"));
        bsOrder.setStartTime(DateUtil.parse(formatCreateDate, "yyyy-MM-dd HH:mm:ss"));
        // 是否夜晚  按夜晚计费
        bsOrder.setEvening("1");
        // 是否支付 未支付  离开得时候支付
        bsOrder.setCharge("0");
        bsOrder.setUserId(userId);
        // 找一个全天没有使用的车位
        List<BsParkInfo> bsParkInfos = parkInfoMapper.selectList(new QueryWrapper<BsParkInfo>().eq("PARK_ID", parkId));
        Iterator<BsParkInfo> iterator = bsParkInfos.iterator();
        while (iterator.hasNext()) {
            BsParkInfo next = iterator.next();
            // 车位编号
            String parkNum = next.getParkNum();
            // 找到当天停车场被使用的车位  需要过滤掉
            List<BsOrder> bsOrders = orderMapper.selectList(new QueryWrapper<BsOrder>()
                    .eq("PARK_ID", parkId)
                    .eq("PARK_INFO_ID", parkNum)
            );
            if (Tools.isNotEmpty(bsOrders)) {
                iterator.remove();
            }
        }
        // 推荐一个空闲车位
        BsParkInfo bsParkInfo = bsParkInfos.get(0);
        // 设置车位号
        bsOrder.setParkInfoId(bsParkInfo.getParkNum());
        // 保存
        int insert = orderMapper.insert(bsOrder);
        int i = 0;
        // 设置订单的价位
        BaseJson priceByOrder = accountStrategy.getPriceByOrder(bsOrder.getFlowId());
        if (priceByOrder.isStatus() && priceByOrder.getData() != null) {
            Double data = (Double) priceByOrder.getData();
            bsOrder.setPrice(data);
            i = orderMapper.updateById(bsOrder);
        } else {
            bsOrder.setPrice(100.00);
            i = orderMapper.updateById(bsOrder);
            throw new RuntimeException("远程调用计算价格失败");
        }
        if (insert != 0 && i != 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int existTempOrder(Map<String, String> bsOrderMap) {
        String parkId = bsOrderMap.get("parkId");
        String carNum = bsOrderMap.get("carNum");
        String userId = bsOrderMap.get("userId");
        int res = 0;
        List<BsOrder> bsOrders = orderMapper.selectList(new QueryWrapper<BsOrder>().eq("PARK_ID", parkId).eq("CAR_NUM", carNum).eq("USER_ID", userId).eq("CHARGE", "0"));

        for (BsOrder bsOrder : bsOrders) {
            String flowId = bsOrder.getFlowId();
            if (flowId.contains("TEMP")) {
                res = 1;
            }
        }
        return res;
    }

    @Transactional
    @Override
    public int updateOrder(Map<String, String> bsOrderMap) {
        String parkId = bsOrderMap.get("parkId");
        String carNum = bsOrderMap.get("carNum");
        String userId = bsOrderMap.get("userId");
        // 同一个停车场同一个人只能有一个待支付的临时订单
        BsOrder bsOrder = orderMapper.selectOne(new QueryWrapper<BsOrder>()
                .eq("PARK_ID", parkId)
                .eq("CAR_NUM", carNum)
                .eq("USER_ID", userId)
                .eq("CHARGE", "0")
                .like("FLOW_ID", "TEMP"));
        Date leaveDate = new Date();
        String leaveTime = DateUtil.format(leaveDate, "yyyy-MM-dd HH:mm:ss");
        // 更新离开时候和是否夜晚
        bsOrder.setLeaveTime(DateUtil.parse(leaveTime, "yyyy-MM-dd HH:mm:ss"));
        if (leaveTime.compareTo("18:00") > 0) {
            bsOrder.setEvening("0");
        }
        // 获取新的价格  更新即可
        int i = 0;
        BaseJson priceByOrder = accountStrategy.getPriceByOrder(bsOrder.getFlowId());
        if (priceByOrder.isStatus() && priceByOrder.getData() != null) {
            Double data = (Double) priceByOrder.getData();
            bsOrder.setPrice(data);
            bsOrder.setCharge("1");
            i = orderMapper.updateById(bsOrder);
        } else {
            throw new RuntimeException("远程调用计算价格失败");
        }
        return i;
    }

}
