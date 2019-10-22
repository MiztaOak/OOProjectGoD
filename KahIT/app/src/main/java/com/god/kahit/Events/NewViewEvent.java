package com.god.kahit.Events;

import com.god.kahit.model.IEvent;

public class NewViewEvent implements IEvent {
    private Class<?> newViewClass;

    public NewViewEvent(Class<?> newViewClass) {
        this.newViewClass = newViewClass;
    }

    public Class<?> getNewViewClass() {
        return newViewClass;
    }
}
