package com.example.abalonegame.config;

import org.springframework.web.socket.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbaloneWebsocketHandler implements WebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Handle a new WebSocket connection
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle a WebSocket transport error
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // Handle a WebSocket connection closed event
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}