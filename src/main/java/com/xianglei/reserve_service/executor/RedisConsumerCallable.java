package com.xianglei.reserve_service.executor;

import com.xianglei.reserve_service.common.BaseJson;
import com.xianglei.reserve_service.domain.BsOrder;
import com.xianglei.reserve_service.mapper.OrderMapper;
import com.xianglei.reserve_service.service.OrderService;
import com.xianglei.reserve_service.service.feigncall.AccountStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    BsOrder bsOrder;

    public void setOrder(BsOrder bsOrder) {
        this.bsOrder = bsOrder;
    }

    @Override
    public Object call() throws Exception {
        int insertOrder = orderService.insertOrder(bsOrder);
        // 更新价位
        BaseJson priceByOrder = accountStrategy.getPriceByOrder(bsOrder.getFlowId());
        if (priceByOrder.isStatus()) {
            Double data = (Double) priceByOrder.getData();
            bsOrder.setPrice(data);
            orderService.updateOrder(bsOrder);
        } else {
            logger.error("远程调用计算价格失败");
            throw new RuntimeException("远程调用计算价格失败");
        }
        return insertOrder;
    }
}
