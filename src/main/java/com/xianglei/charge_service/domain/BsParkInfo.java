package com.xianglei.charge_service.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;


@TableName(value ="BS_PARK_INFO")
public class BsParkInfo implements Serializable, Cloneable {
    /**
     * 车位id
     */
    @TableId("FLOW_ID")
    private String flowId;
    /**
     * 该停车位编号
     */
    @TableField("PARK_NUM")
    private String parkNum;
    /**
     * 0/1/2 不可用/有车/预约
     */
    @TableField("STATUS")
    private String status;
    /**
     * 车位长度
     */
    @TableField("LENGTH")
    private Double length;
    /**
     * 车位宽度
     */
    @TableField("WIDTH")
    private Double width;
    /**
     * 停车场id
     */
    @TableField("PARK_ID")
    private String parkId;
    /**
     * 临时拥有者
     */
    @TableField("TEMP_OWNER")
    private String tempOwner;
    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getParkNum() {
        return parkNum;
    }

    public void setParkNum(String parkNum) {
        this.parkNum = parkNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }
    public String getTempOwner() {
        return tempOwner;
    }

    public void setTempOwner(String tempOwner) {
        this.tempOwner = tempOwner;
    }
}