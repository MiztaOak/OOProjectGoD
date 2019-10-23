package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

public class MyPlayerIdChangedEvent implements IEvent {
    private String newPlayerId;

    public MyPlayerIdChangedEvent(String newPlayerId) {
        this.newPlayerId = newPlayerId;
    }

    public String getNewPlayerId() {
        return newPlayerId;
    }
}
