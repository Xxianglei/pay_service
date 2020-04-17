package com.xianglei.charge_service.common.utils;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：数据参数封装
 * 时间：[2019/12/2:22:08]
 * 作者：xianglei
 * params: * @param null
 */
@Component
public class ListParamUtils<T> {

    /**
     * 专门处理前端传入数组参数
     */
    List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
