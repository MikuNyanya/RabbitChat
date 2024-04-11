package cn.mikulink.rabbitchat.entity.response.chat;

import cn.mikulink.rabbitchat.entity.db.ChatRecordInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * created by MikuNyanya on 2024/4/12 4:26
 * For the Reisen
 */
@Getter
@Setter
public class ChatRecordVo extends ChatRecordInfo {
    //消息发出人名称
    private String fromUserName;
    //消息发出人头像
    private String fromUserImg;
}
