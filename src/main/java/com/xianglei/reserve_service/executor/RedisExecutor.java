package com.xianglei.reserve_service.executor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xianglei.reserve_service.common.utils.RedisUtil;
import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.domain.BsParkInfo;
import com.xianglei.reserve_service.mapper.OrderMapper;
import com.xianglei.reserve_service.mapper.ParkInfoMapper;
import com.xianglei.reserve_service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 线程池
 */
@Component
public class RedisExecutor {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedisConsumerCallable redisConsumerCallable;
    @Autowired
    ParkInfoMapper parkInfoMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderMapper orderMapper;
    private Logger logger = LoggerFactory.getLogger(RedisExecutor.class);
    private int maxThreadNum = 0;
    // 只实例化了一次 单例
    private ExecutorService pool;
    ThreadFactory threadFactory;
    LinkedBlockingQueue containerQueue;
    int cpuThreadNums = 1;

    public RedisExecutor() {
        // cpu密集
        cpuThreadNums = Runtime.getRuntime().availableProcessors();
        containerQueue = new LinkedBlockingQueue<>(cpuThreadNums);
        threadFactory = new ThreadFactoryBuilder().setNameFormat("redis-task-consumer-%d").build();
        maxThreadNum = cpuThreadNums + 1;
        pool = new ThreadPoolExecutor(cpuThreadNums, maxThreadNum,
                0L, TimeUnit.MILLISECONDS,
                containerQueue, threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
    /**
     * 从消息队列中取订单信息插入到批量数据库
     *
     * @param order
     */
    public int consume(BsOrder order) {
        redisConsumerCallable.setOrder(order);
        Future<Integer> submit = pool.submit(redisConsumerCallable);
        Integer result = null;
        try {
            result = submit.get();
        } catch (InterruptedException e) {
            // 回滚
            BsParkInfo bsParkInfo = parkInfoMapper.selectOne(new QueryWrapper<BsParkInfo>().eq("PARK_NUM",order.getParkInfoId() ).eq("PARK_ID", order.getParkId()));
            orderService.releaseParkInfo(bsParkInfo.getFlowId(), order.getUserId());
            orderMapper.deleteById(order.getFlowId());
            e.printStackTrace();
            logger.error("线程池中断异常{}", e);
        } catch (ExecutionException e) {
            // 回滚
            BsParkInfo bsParkInfo = parkInfoMapper.selectOne(new QueryWrapper<BsParkInfo>().eq("PARK_NUM",order.getParkInfoId() ).eq("PARK_ID", order.getParkId()));
            orderService.releaseParkInfo(bsParkInfo.getFlowId(), order.getUserId());
            orderMapper.deleteById(order.getFlowId());
            e.printStackTrace();
            logger.error("线程池执行任务失败{}", e);
        }
        return result;
    }
}
