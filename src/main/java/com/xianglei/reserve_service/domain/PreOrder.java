package com.xianglei.reserve_service.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 21:46
 * com.xianglei.charge_service.domain
 * @Description:前端交互 更具停车场查看订单信息
 */
public class PreOrder {
    String name;
    String carNum;
    String color;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date endDate;
    String parkName;
    String parkNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkNo() {
        return parkNo;
    }

    public void setParkNo(String parkNo) {
        this.parkNo = parkNo;
    }
}
