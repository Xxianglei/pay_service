package com.xianglei.reserve_service.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 20:59
 * com.xianglei.reserve_service.domain
 * @Description:
 */
@TableName(value ="BS_MESSAGE")
public class BsMessage implements Serializable {
    @TableId("FLOW_ID")
    String flowId;
    @TableField("CREATE_DATE")
    Date createDate;
    @TableField("TX_ID")
    String txId;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
