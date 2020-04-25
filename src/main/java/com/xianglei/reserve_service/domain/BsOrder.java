package com.xianglei.reserve_service.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "BS_ORDER")
public class BsOrder implements Serializable, Cloneable {

    @TableId("FLOW_ID")
    private String flowId;
    /**
     * 用户id
     */
    @TableField("USER_ID")
    private String userId;
    /**
     * 订单创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("START_TIME")
    private Date startTime;
    /**
     * 离开时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("LEAVE_TIME")
    private Date leaveTime;
    /**
     * 0/1 白天/夜间
     */
    @TableField("EVENING")
    private String evening;
    /**
     * 车牌号
     */
    @TableField("CAR_NUM")
    private String carNum;
    /**
     * 停车场id
     */
    @TableField("PARK_ID")
    private String parkId;
    /**
     * 订单状态
     */
    @TableField("CHARGE")
    private String charge;
    /**
     *
     */
    @TableField("PRICE")
    private Double price;

    /**
     * 车位id
     */
    @TableField("PARK_INFO_ID")
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}