package com.xianglei.charge_service.domain;

import java.io.Serializable;

public class PreBsOrder implements Serializable, Cloneable {

    private String flowId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 订单创建时间
     */
    private String createTime;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 离开时间
     */
    private String leaveTime;
    /**
     * 离开时间
     */
    private String parkName;
    /**
     * 离开时间
     */
    private String location;
    /**
     * 离开时间
     */
    private Double price;
    /**
     * 0/1 白天/夜间
     */
    private String evening;
    /**
     * 车牌号
     */
    private String carNum;
    /**
     * 停车场id
     */
    private String parkId;
    /**
     * 订单状态
     */
    private String charge;

    /**
     * 车位id
     */
    private String parkInfoId;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getParkInfoId() {
        return parkInfoId;
    }

    public void setParkInfoId(String parkInfoId) {
        this.parkInfoId = parkInfoId;
    }
}