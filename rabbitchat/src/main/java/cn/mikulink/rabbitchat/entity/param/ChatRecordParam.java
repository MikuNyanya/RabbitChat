package cn.mikulink.rabbitchat.entity.param;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * MikuLink created in 2024/4/12 2:29
 * For the Reisen
 * <p>
 * 聊天记录参数
 */
@Getter
@Setter
@Alias("chatRecordParam")
public class ChatRecordParam {
    private Long toId;

    private int page=1;
    private int limitStart=0;
    private int pageSize = 10;
}
