package com.xianglei.charge_service.service;

import com.xianglei.charge_service.domain.BsMessage;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 21:02
 * com.xianglei.charge_service.service
 * @Description:消息sevice
 */
public interface MessageService {
    int insertMessage(BsMessage bsMessage);
    BsMessage getMessageById(String txId);
}
