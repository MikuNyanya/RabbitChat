package cn.mikulink.rabbitchat;

import cn.mikulink.rabbitchat.websocket.WebSocketRabbitClient;
import org.java_websocket.enums.ReadyState;
import org.junit.Test;

import java.net.URI;

/**
 * MikuLink created in 2024/2/16 7:57
 * For the Reisen
 */
public class WebSocketTest {
    private String webSocketUrl = "ws://localhost:21010/websocket/1001";

    @Test
    public void test() {
        try {
            WebSocketRabbitClient client = new WebSocketRabbitClient(new URI(webSocketUrl));
            client.setConnectionLostTimeout(5000);
            client.connect();

            while (!client.getReadyState().equals(ReadyState.OPEN)){
                System.out.println("正在连接...");

                Thread.sleep(100);
            }

            client.send("测试连接");

            System.out.println("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("===end===");
    }

}
