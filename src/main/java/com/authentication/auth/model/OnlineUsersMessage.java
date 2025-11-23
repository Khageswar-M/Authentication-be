package com.authentication.auth.model;

import java.util.Set;

public class OnlineUsersMessage {
    private Set<String> onlineUsers;

    public OnlineUsersMessage(Set<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(Set<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
}
