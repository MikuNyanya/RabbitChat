package cn.mikulink.rabbitchat.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * MikuLink created in 2024/2/18 4:58
 * For the Reisen
 *
 * 会话授权信息
 */
@Getter
@Setter
public class ChatAuthInfo {
    private String createDate;
    private String uid;

}
