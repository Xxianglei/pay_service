package com.xianglei.reserve_service.message;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xianglei.reserve_service.common.utils.RedisUtil;
import com.xianglei.reserve_service.common.utils.Tools;
import com.xianglei.reserve_service.domain.BsMessage;
import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.domain.BsParkInfo;
import com.xianglei.reserve_service.executor.RedisExecutor;
import com.xianglei.reserve_service.mapper.ParkInfoMapper;
import com.xianglei.reserve_service.service.MessageService;
import com.xianglei.reserve_service.service.OrderService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/14 14:19
 * com.xianglei.reserve_service.message
 * @Description:订单消费
 */

@Component
@RocketMQMessageListener(consumerGroup = "CONSUMER_GROUP", topic = "ORDER_TOPIC", messageModel = MessageModel.CLUSTERING)
public class OrderConsumer implements RocketMQListener<BsOrder>, RocketMQPushConsumerLifecycleListener {

    @Autowired
    RedisExecutor redisExecutor;
    @Autowired
    MessageService messageService;
    @Autowired
    OrderService orderService;
    @Autowired
    ParkInfoMapper parkInfoMapper;
    @Autowired
    RedisUtil redisUtil;
    private Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @Override
    public void onMessage(BsOrder s) {
        BsOrder order = s;
        try {
            logger.info("消费端消费任务插入数据库");
            BsMessage bsMessage = new BsMessage();
            /**
             * 消费幂等处理
             */
            if (Tools.isNotNull(order)) {
                BsMessage messageById = messageService.getMessageById(order.getFlowId());
                if (Tools.isNull(messageById)) {
                    // 任务出队 执行失败直接丢弃任务更新状态
                    int consume = redisExecutor.consume(order);
                    // 如果消费成功  则放入redis 设置过期时间  k为订单id  半小时订单过期
                    boolean setOk = redisUtil.set(order.getFlowId(), order.getFlowId(), 1000 * 60 * 30);
                    if (consume != 0 && setOk) {
                        bsMessage.setFlowId(UUID.randomUUID().toString());
                        bsMessage.setTxId(order.getFlowId());
                        messageService.insertMessage(bsMessage);
                        logger.info("消费端消费任务提交线程池成功");
                    } else {
                        /**
                         * 车位释放
                         */
                        BsParkInfo bsParkInfo = parkInfoMapper.selectOne(new QueryWrapper<BsParkInfo>().eq("PARK_NUM",order.getParkInfoId() ).eq("PARK_ID", order.getParkId()));
                        orderService.releaseParkInfo(bsParkInfo.getFlowId(), order.getUserId());
                        logger.error("消费端消费任务提交线程池失败");
                    }
                }
            }

        } catch (Exception e) {
            BsParkInfo bsParkInfo = parkInfoMapper.selectOne(new QueryWrapper<BsParkInfo>().eq("PARK_NUM",order.getParkInfoId() ).eq("PARK_ID", order.getParkId()));
            orderService.releaseParkInfo(bsParkInfo.getFlowId(), order.getUserId());
            logger.error("消息队列任务出队失败：{}", e);
        }

    }

    /**
     * 消费失败的情况下重复消费3次
     *
     * @param defaultMQPushConsumer
     */
    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        defaultMQPushConsumer.setMaxReconsumeTimes(1);
    }

}