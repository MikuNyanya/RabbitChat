package cn.mikulink.rabbitchat.service;

import cn.mikulink.rabbitchat.entity.db.ChatRecordInfo;
import cn.mikulink.rabbitchat.entity.param.ChatRecordParam;
import cn.mikulink.rabbitchat.entity.response.MethodReInfo;
import cn.mikulink.rabbitchat.mapper.ChatRecordMapper;
import cn.mikulink.rabbitchat.utils.PageUtil;
import cn.mikulink.rabbitchat.utils.StringUtil;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 创建一条消息记录
     *
     * @param info 消息记录
     */
    public void create(ChatRecordInfo info) {
        mapper.create(info);
    }


    /**
     * 聊天记录列表
     * @param param
     * @return
     */
    public List<ChatRecordInfo> getChatRecordHistory(ChatRecordParam param){
        param.setLimitStart(PageUtil.getLimit(param.getPage(),param.getPageSize()));
        return mapper.getChatRecordHistory(param);
    }



    /**
     * 消息记录参数检查
     *
     * @param info 消息记录
     * @return 检查结果
     */
    public MethodReInfo paramCheck(ChatRecordInfo info) {
        if (null == info) {
            return MethodReInfo.create(false, "消息记录不能为空");
        }
        if (null == info.getType()) {
            return MethodReInfo.create(false, "消息类型异常");
        }
        if (info.getType() == 0 && null == info.getFromId()) {
            return MethodReInfo.create(false, "发送人信息异常");
        }
        if (info.getType() == 0 && null == info.getToId()) {
            return MethodReInfo.create(false, "接收人信息异常");
        }
        if (StringUtil.isEmpty(info.getContent())) {
            return MethodReInfo.create(false, "消息内容为空!");
        }
        return MethodReInfo.create();
    }
}
