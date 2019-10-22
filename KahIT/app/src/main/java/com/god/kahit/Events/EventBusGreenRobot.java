package com.god.kahit.Events;

import com.god.kahit.model.IEventBus;
import com.god.kahit.model.IEvent;

public class EventBusGreenRobot implements IEventBus {
    public static final org.greenrobot.eventbus.EventBus BUS = new org.greenrobot.eventbus.EventBus();

    @Override
    public void post(IEvent event) {
        BUS.post(event);
    }
}
