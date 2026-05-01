package dev.ismoil.chat_service.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler  extends TextWebSocketHandler {

    private Map<String, WebSocketSession> webSocketSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected to WebSocket" + session.getId());

        this.webSocketSessions.put(session.getId(), session);
        session.sendMessage(new org.springframework.web.socket.TextMessage("Hello from WebSocket"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        log.info("Received message:  " + message.getPayload());
        this.webSocketSessions.values().forEach(
                webSocketSession ->
                {
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

        log.info("Disconnected from WebSocket" + session.getId());

        this.webSocketSessions.remove(session.getId());
    }
}
