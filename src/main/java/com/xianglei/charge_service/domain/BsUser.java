package com.xianglei.charge_service.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;


@TableName(value ="BS_USER")
public class BsUser implements Serializable, Cloneable {
    /**
     * 流水号
     */
    @TableId("FLOW_ID")
    private String flowId;
    /**
     * 姓名
     */
    @TableField("NAME")
    private String name;
    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;
    /**
     * 账号
     */
    @TableField("ACCOUNT")
    private String account;
    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private Date createDate;
    /**
     * 手机号
     */
    @TableField("PHONE")
    private String phone;
    /**
     * 0/1下线/上线
     */
    @TableField("STATUS")
    private String status;
    /**
     * 0/1 会员/非会员
     */
    @TableField("VIP")
    private String vip;
    /**
     * 是否是超级管理员
     */
    @TableField("SUPER_ROOT")
    private String superRoot;
    /**
     * 年龄
     */
    @TableField("AGE")
    private Integer age;
    /**
     * 性别0/1 男/女
     */
    @TableField("SEXY")
    private String sexy;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getSuperRoot() {
        return superRoot;
    }

    public void setSuperRoot(String superRoot) {
        this.superRoot = superRoot;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSexy() {
        return sexy;
    }

    public void setSexy(String sexy) {
        this.sexy = sexy;
    }
}