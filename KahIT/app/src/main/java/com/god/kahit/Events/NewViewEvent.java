package com.god.kahit.Events;

public class NewViewEvent {
    private Class<?> newViewClass;

    public NewViewEvent(Class<?> newViewClass) {
        this.newViewClass = newViewClass;
    }

    public Class<?> getNewViewClass() {
        return newViewClass;
    }
}
