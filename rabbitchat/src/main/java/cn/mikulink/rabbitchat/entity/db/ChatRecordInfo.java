package cn.mikulink.rabbitchat.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * MikuLink created in 2024/2/25 14:38
 * For the Reisen
 * <p>
 * 消息记录
 */
@Getter
@Setter
@AllArgsConstructor //有参构造
@NoArgsConstructor  //无参构造
@Alias("chatRecord")
public class ChatRecordInfo {
    /**
     * 数据id
     */
    private Long id;
    /**
     * 消息发送时间
     */
    private Date createTime;
    /**
     * 业务类型
     * 0.聊天记录
     */
    private Integer type;
    /**
     * 消息状态
     * 0.服务器已接收但还未向目标发送
     * 1.正在向目标发送
     * 2.已向目标完成发送
     * 3.发送时遇到异常需要重新发送
     * 4.废弃该条消息
     */
    private Integer status;
    /**
     * 消息来源id(用户id)
     */
    private Long fromId;
    /**
     * 消息目标id(用户id)
     */
    private Long toId;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 已读状态
     * 0.未读 1.已读
     */
    private Integer readStatus;
    /**
     * 已读时间
     */
    private Date readTime;
}
