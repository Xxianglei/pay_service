package com.xianglei.charge_service.service.impl;

import com.xianglei.charge_service.domain.BsMessage;
import com.xianglei.charge_service.mapper.MessageMapper;
import com.xianglei.charge_service.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: Xianglei
 * @Company: xxx
 * @Date: 2020/4/17 21:03
 * com.xianglei.charge_service.service.impl
 * @Description:
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Override
    public int insertMessage(BsMessage bsMessage) {
        int insert = messageMapper.insert(bsMessage);
        return insert;
    }

    @Override
    public BsMessage getMessageById(String txId) {
        return null;
    }
}
