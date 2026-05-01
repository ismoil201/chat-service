package dev.ismoil.chat_service.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j // bu log chiqarish uchun
@Component // Spring container qo'shish uchun ya'ni Bean qilib beradi
public class WebSocketHandler extends TextWebSocketHandler {


    final Map<String, WebSocketSession> webSocketSessions = new HashMap<>();



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.info("Connected" , session.getId());

        this.webSocketSessions.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("{} sent : {}", session.getId(),  message.getPayload());

        this.webSocketSessions.values().forEach(
                webSocketSession -> {
                    try {
                        webSocketSession.sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Connection closed", session.getId());

        this.webSocketSessions.remove(session.getId());
    }

}
