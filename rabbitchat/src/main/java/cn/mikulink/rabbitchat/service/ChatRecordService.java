package cn.mikulink.rabbitchat.service;

import cn.mikulink.rabbitchat.entity.db.ChatRecordInfo;
import cn.mikulink.rabbitchat.mapper.ChatRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MikuLink created in 2024/2/25 5:18
 * For the Reisen
 * <p>
 * 消息记录服务
 */
@Service
@Slf4j
public class ChatRecordService {
    @Autowired
    private ChatRecordMapper mapper;

    public void create(ChatRecordInfo info) {
        mapper.create(info);
    }
}
