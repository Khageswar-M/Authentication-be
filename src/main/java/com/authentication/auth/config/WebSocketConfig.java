package com.authentication.auth.config;

import com.authentication.auth.handler.UserPresenceHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final UserPresenceHandler userPresenceHandler;

    public WebSocketConfig(UserPresenceHandler userPresenceHandler) {
        this.userPresenceHandler = userPresenceHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(userPresenceHandler, "/ws/presence")
                .setAllowedOrigins("*"); // for frontend localhost
    }
}
