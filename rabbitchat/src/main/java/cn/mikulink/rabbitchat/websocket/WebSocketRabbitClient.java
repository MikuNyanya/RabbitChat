package cn.mikulink.rabbitchat.websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * MikuLink created in 2024/2/16 7:55
 * For the Reisen
 * <p>
 * websocket客户端 用于测试
 */
@Slf4j
public class WebSocketRabbitClient extends WebSocketClient {
    public WebSocketRabbitClient(URI serverUri) {
        super(serverUri);
    }

    /**
     * 打开链接
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("客户端websocket连接已打开");
    }

    /**
     * 收到消息
     */
    @Override
    public void onMessage(String s) {
        log.info("客户端websocket接收消息:{}", s);
    }

    /**
     * 关闭连接
     */
    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("客户端websocket关闭连接");
    }

    /**
     * 发生错误
     */
    @Override
    public void onError(Exception ex) {
        log.error("客户端websocket连接发生错误", ex);
    }
}
