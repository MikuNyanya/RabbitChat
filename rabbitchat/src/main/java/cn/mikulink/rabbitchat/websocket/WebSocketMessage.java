package cn.mikulink.rabbitchat.websocket;

import lombok.Getter;
import lombok.Setter;

/**
 * MikuLink created in 2024/2/18 8:31
 * For the Reisen
 * <p>
 * websocket消息传递对象
 * 与客户端传递消息时，所有消息都应使用此对象转化的json字符串
 */
@Getter
@Setter
public class WebSocketMessage {
    public WebSocketMessage() {

    }

    public WebSocketMessage(String chatAuth, Integer messageSendType,String fromUid,String fromGroupId, String toUid, String toGroupId,
                            String messageStatus, String sendDateStr, String message) {
        this.chatAuth = chatAuth;
        this.messageSendType = messageSendType;
        this.toUid = toUid;
        this.toGroupId = toGroupId;
        this.messageStatus = messageStatus;
        this.sendDateStr = sendDateStr;
        this.message = message;
    }


    /**
     * 会话权限码
     */
    private String chatAuth;
    /**
     * 动作类型
     * 1.发送到用户 2.发送到群
     */
    private Integer messageSendType;
    /**
     * 消息来源用户id
     * 服务器消息为0
     * 用户消息为对应的用户uid
     */
    private String fromUid;
    /**
     * 消息来源群id
     */
    private String fromGroupId;
    /**
     * 用户id 当messageSendType为1时，该值为目标用户id
     */
    private String toUid;
    /**
     * 群id 当messageSendType为2时，该值为目标群id
     */
    private String toGroupId;
    /**
     * 状态码
     * SUCCESS 正常
     * NO_AUTH 没有权限
     * ERROR 异常
     */
    private String messageStatus;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 该消息发送的时间
     */
    private String sendDateStr;
}
