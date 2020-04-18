package com.xianglei.charge_service.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;


@TableName(value ="BS_PARK")
public class BsPark implements Serializable, Cloneable {
    /**
     *
     */
    @TableId("FLOW_ID")
    private String flowId;
    /**
     * 停车场名字
     */
    @TableField("PARK_NAME")
    private String parkName;
    /**
     * 白天价格
     */
    @TableField("B_PRICE")
    private Double bPrice;
    /**
     * 晚上价格
     */
    @TableField("Y_PRICE")
    private Double yPrice;
    /**
     * vip用户的价格
     */
    @TableField("V_PRICE")
    private Double vPrice;
    /**
     * 停车场最大容量
     */
    @TableField("VOLUME")
    private Integer volume;
    /**
     * 已经停放的车辆数
     */
    @TableField("IN_USED")
    private Integer inUsed;
    /**
     * 地址
     */
    @TableField("LOCATION")
    private String location;
    /**
     * 经度
     */
    @TableField("JD")
    private Double jd;
    /**
     * 维度
     */
    @TableField("WD")
    private Double wd;
    /**
     * 默认日期
     */
    @TableField("CREATE_DATE")
    private Date createDate;
    /**
     * 计费策略（冗余）
     */
    @TableField("STRATAGE")
    private String stratage;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public Double getbPrice() {
        return bPrice;
    }

    public void setbPrice(Double bPrice) {
        this.bPrice = bPrice;
    }

    public Double getyPrice() {
        return yPrice;
    }

    public void setyPrice(Double yPrice) {
        this.yPrice = yPrice;
    }

    public Double getvPrice() {
        return vPrice;
    }

    public void setvPrice(Double vPrice) {
        this.vPrice = vPrice;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getInUsed() {
        return inUsed;
    }

    public void setInUsed(Integer inUsed) {
        this.inUsed = inUsed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getJd() {
        return jd;
    }

    public void setJd(Double jd) {
        this.jd = jd;
    }

    public Double getWd() {
        return wd;
    }

    public void setWd(Double wd) {
        this.wd = wd;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStratage() {
        return stratage;
    }

    public void setStratage(String stratage) {
        this.stratage = stratage;
    }
}