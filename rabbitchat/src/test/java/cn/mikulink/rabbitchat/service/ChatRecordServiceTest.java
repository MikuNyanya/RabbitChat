package cn.mikulink.rabbitchat.service;

import cn.mikulink.rabbitchat.entity.db.ChatRecordInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatRecordServiceTest {
    @Autowired
    private ChatRecordService chatRecordService;

    @Test
    public void create(){
        ChatRecordInfo info = new ChatRecordInfo();
        info.setCreateTime(new Date());
        info.setType(0);
        info.setStatus(0);
        info.setFromId(0L);
        info.setToId(0L);
        info.setContent("测试内容");
        info.setReadStatus(0);
        info.setReadTime(null);

        try {
            chatRecordService.create(info);
            System.out.println("");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
