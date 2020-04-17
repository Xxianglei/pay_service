package com.xianglei.charge_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xianglei.charge_service.domain.BsParkInfo;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 14:26
 * com.xianglei.charge_service.mapper
 * @Description:车位信息service
 */
@Repository
public interface ParkInfoMapper extends BaseMapper<BsParkInfo> {
}
