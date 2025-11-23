package com.authentication.auth.handler;

import com.authentication.auth.model.OnlineUsersMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserPresenceHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = extractUserId(session);
        if (userId != null) {
            sessions.put(userId, session);
            System.out.println("✅ Connected: " + userId);
            broadcastStatus();
        } else {
            System.out.println("❌ Could not extract userId from URI: " + session.getUri());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = extractUserId(session);
        if (userId != null) {
            sessions.remove(userId);
            System.out.println("❌ Disconnected: " + userId);
            broadcastStatus();
        }
    }

    private String extractUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("userId=")) {
            String id = query.substring(query.indexOf("userId=") + 7);
            return id.toLowerCase(); // ✅ normalize
        }
        return null;
    }

    private void broadcastStatus() throws IOException {
        Set<String> onlineUsers = new HashSet<>();
        for (String id : sessions.keySet()) {
            onlineUsers.add(id.toLowerCase());
        }
        OnlineUsersMessage message = new OnlineUsersMessage(onlineUsers);
        String json = objectMapper.writeValueAsString(message);

        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(json));
            }
        }

        System.out.println("📡 Broadcast Online Users: " + onlineUsers);
    }
}
