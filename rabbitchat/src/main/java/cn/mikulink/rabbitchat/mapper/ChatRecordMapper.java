package cn.mikulink.rabbitchat.mapper;

import cn.mikulink.rabbitchat.entity.db.ChatRecordInfo;
import cn.mikulink.rabbitchat.entity.db.UsersInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * MikuLink created in 2024/2/25 14:44
 * For the Reisen
 * <p>
 * mapper容器
 * 消息记录
 */
@Mapper
public interface ChatRecordMapper {

    void create(ChatRecordInfo param);

    ChatRecordInfo getById(Long id);

}
