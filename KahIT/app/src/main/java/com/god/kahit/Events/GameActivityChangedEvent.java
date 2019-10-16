package com.god.kahit.Events;

public class GameActivityChangedEvent {
    private String newActivityId;

    public GameActivityChangedEvent(String newActivityId) {
        this.newActivityId = newActivityId;
    }

    public String getNewActivityId() {
        return newActivityId;
    }
}
