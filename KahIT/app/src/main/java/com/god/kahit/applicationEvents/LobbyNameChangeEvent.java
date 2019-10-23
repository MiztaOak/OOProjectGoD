package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

public class LobbyNameChangeEvent implements IEvent {
    private String lobbyName;

    public LobbyNameChangeEvent(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }
}
