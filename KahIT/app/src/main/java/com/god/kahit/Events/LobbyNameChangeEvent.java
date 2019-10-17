package com.god.kahit.Events;

public class LobbyNameChangeEvent {
    private String lobbyName;

    public LobbyNameChangeEvent(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }
}
