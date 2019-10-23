package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

public class GameActivityChangedEvent implements IEvent {
    private String newActivityId;

    public GameActivityChangedEvent(String newActivityId) {
        this.newActivityId = newActivityId;
    }

    public String getNewActivityId() {
        return newActivityId;
    }
}
