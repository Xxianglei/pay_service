package com.xianglei.reserve_service.common;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 14:36
 * com.xianglei.reserve_service.common
 * @Description:
 */
public enum DateEnum {
    NIGHT("晚上"),
    DAY("白天");
    private final String name;

    DateEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
