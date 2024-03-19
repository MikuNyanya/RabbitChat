package cn.mikulink.rabbitchat;

import cn.mikulink.rabbitchat.utils.DateUtil;
import cn.mikulink.rabbitchat.websocket.WebSocketMessage;
import cn.mikulink.rabbitchat.websocket.WebSocketRabbitClient;
import com.alibaba.fastjson.JSONObject;
import org.java_websocket.enums.ReadyState;
import org.junit.Test;

import java.net.URI;
import java.util.Date;

/**
 * MikuLink created in 2024/2/16 7:57
 * For the Reisen
 */
public class WebSocketTest {
    private int sid = 1;
    private int toId = 1;
    private String authStr = "FjMmc0CNlzWI4WsaaHGO0Yhz/gm4pEpjpBsxM24awr2/xBoiAh0tpw==";
    //ws://localhost:21010/websocket/1?chatAuth=FjMmc0CNlzWI4WsaaHGO0Yhz/gm4pEpjpBsxM24awr2/xBoiAh0tpw==
    private String webSocketUrl = "ws://localhost:21010/websocket/";

    @Test
    public void test() {
        try {
            //携带会话权限码
            webSocketUrl += sid;
            webSocketUrl += "?chatAuth=" + authStr;
            WebSocketRabbitClient client = new WebSocketRabbitClient(new URI(webSocketUrl));
            client.setConnectionLostTimeout(5000);
            client.connect();


            while (!client.getReadyState().equals(ReadyState.OPEN)) {
                System.out.println("正在连接...");

                Thread.sleep(100);
            }

            WebSocketMessage returnMsg = new WebSocketMessage(authStr, 1, String.valueOf(sid),
                    null, String.valueOf(toId), null, "SUCCESS", DateUtil.toString(new Date()),
                    "这是一条客户端消息" + DateUtil.toString(new Date()));

            client.send(JSONObject.toJSONString(returnMsg));

            System.out.println("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("===end===");
    }

}
