package dev.ismoil.chat_service.config;


import dev.ismoil.chat_service.handlers.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@RequiredArgsConstructor
@EnableWebSocket
@Configuration
public class WebSocketConfiguration  implements WebSocketConfigurer {

    final WebSocketHandler webSocketChatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(webSocketChatHandler, "/ws/chats");
    }
}
