package com.xianglei.charge_service.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;


@TableName("BS_USER_CAR")
public class BsUserCar implements Serializable, Cloneable {
    /**
     *
     */
    @TableId
    private String flowId;
    /**
     * 车牌号
     */
    @TableField
    private String carNum;
    /**
     * 颜色
     */
    @TableField
    private String color;
    /**
     * 车型号
     */
    @TableField
    private String model;
    /**
     * 车主id
     */
    @TableField
    private String userId;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}