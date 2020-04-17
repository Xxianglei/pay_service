package com.xianglei.charge_service.executor;

import com.xianglei.charge_service.domain.BsOrder;
import com.xianglei.charge_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 18:47
 * com.xianglei.charge_service.executor
 * @Description:线程方法批量插入数据库
 */
@Component
public class RedisConsumerCallable implements Callable {

    @Autowired
    OrderService orderService;
    BsOrder bsOrder;

    public void setOrder(BsOrder bsOrder) {
        this.bsOrder = bsOrder;
    }

    @Override
    public Object call() throws Exception {
        int insertOrder = orderService.insertOrder(bsOrder);
        return insertOrder;
    }
}
