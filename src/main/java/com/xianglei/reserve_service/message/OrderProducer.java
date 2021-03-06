package com.xianglei.reserve_service.message;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xianglei.reserve_service.common.MessageTopic;
import com.xianglei.reserve_service.common.utils.Tools;
import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.domain.BsParkInfo;
import com.xianglei.reserve_service.mapper.OrderMapper;
import com.xianglei.reserve_service.mapper.ParkInfoMapper;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/14 14:20
 * com.xianglei.reserve_service.message
 * @Description:订单生成
 */
@Component
public class OrderProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private ParkInfoMapper parkInfoMapper;
    @Autowired
    OrderMapper orderMapper;

    /**
     * 任务同步发送
     *
     * @param bsOrder
     */
    @Transactional
    public SendResult sendOrder(BsOrder bsOrder) {
        /* 获取用户id */
        /*前端需要传入 除了parkinfoid charge   FLOW_ID*/
        SendResult sendResult = null;
        String userId = bsOrder.getUserId();
        BsParkInfo parkInfo = parkInfoMapper.selectOne(new QueryWrapper<BsParkInfo>().like("TEMP_OWNER", userId).eq("PARK_ID", bsOrder.getParkId()).eq("PARK_NUM", bsOrder.getParkInfoId()));
        // 车位占用成功 后续根据这个按序生成订单
        if (Tools.isNotNull(parkInfo)) {
            // 设置订单编号
            bsOrder.setFlowId(UUID.randomUUID().toString());
            // 发送到消息队列(消费端插入数据库)  消费端处理支付信息
            sendResult = rocketMQTemplate.syncSend(MessageTopic.ORDER.getName(), bsOrder);
        } else {
            // 车位占用失败
            sendResult.setSendStatus(SendStatus.SLAVE_NOT_AVAILABLE);
        }
        return sendResult;
    }
}
