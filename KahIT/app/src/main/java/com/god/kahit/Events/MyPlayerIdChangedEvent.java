package com.god.kahit.Events;

public class MyPlayerIdChangedEvent {
    private String newPlayerId;

    public MyPlayerIdChangedEvent(String newPlayerId) {
        this.newPlayerId = newPlayerId;
    }

    public String getNewPlayerId() {
        return newPlayerId;
    }
}
