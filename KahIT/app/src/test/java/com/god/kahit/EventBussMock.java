package com.god.kahit;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.IEventBus;

import java.util.ArrayList;
import java.util.List;

public class EventBussMock implements IEventBus {

    private static List<IEvent> eventList = new ArrayList<>();

    @Override
    public void post(IEvent event) {
        EventBussMock.eventList.add(event);
    }

    public static List<IEvent> getEventList() {
        return eventList;
    }

}
