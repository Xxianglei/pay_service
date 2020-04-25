package com.xianglei.reserve_service.service;

import com.xianglei.reserve_service.domain.PreOrder;

import java.util.List;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 21:45
 * com.xianglei.charge_service.service
 * @Description:停车场service
 */
public interface ParkService {

    List<PreOrder> getPark(String name);
}
