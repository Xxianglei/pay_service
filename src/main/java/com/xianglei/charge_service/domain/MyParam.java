package com.xianglei.charge_service.domain;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 18:59
 * com.xianglei.charge_service.domain
 * @Description:参数封装接收
 */
public class MyParam {
    String flowId;
    String userId;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
