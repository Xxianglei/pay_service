package com.xianglei.reserve_service.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;


@TableName(value ="BS_USER_CAR")
public class BsUserCar implements Serializable, Cloneable {
    /**
     *
     */
    @TableId("FLOW_ID")
    private String flowId;
    /**
     * 车牌号
     */
    @TableField("CAR_NUM")
    private String carNum;
    /**
     * 颜色
     */
    @TableField("COLOR")
    private String color;
    /**
     * 车型号
     */
    @TableField("MODEL")
    private String model;
    /**
     * 车主id
     */
    @TableField("USER_ID")
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