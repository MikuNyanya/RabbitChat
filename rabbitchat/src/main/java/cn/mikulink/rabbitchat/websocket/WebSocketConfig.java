package cn.mikulink.rabbitchat.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * MikuLink created in 2024/2/16 5:39
 * For the Reisen
 * <p>
 * WebSocket配置
 */
@Configuration
@Slf4j
public class WebSocketConfig {

    /**
     * bean注册：会自动扫描带有@ServerEndpoint注解声明的Websocket Endpoint(端点)，注册成为Websocket bean。
     * 要注意，如果项目使用外置的servlet容器，而不是直接使用springboot内置容器的话，就不要注入ServerEndpointExporter，因为它将由容器自己提供和管理。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        log.info("=====启动WebSocketService=====");
        return new ServerEndpointExporter();
    }
}
