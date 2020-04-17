package com.xianglei.charge_service.common;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 15:15
 * com.xianglei.charge_service.common
 * @Description:
 */
public enum MessageTopic {

    ORDER("ORDER-TOPIC"),
    KEY("ORDER");
    private final String name;

    MessageTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
