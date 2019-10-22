package com.god.kahit.Events;

import com.god.kahit.model.IEvent;
import com.god.kahit.networkManager.Connection;

import java.util.List;

public class RoomChangeEvent implements IEvent {
    private List<Connection> roomsConnection;

    public RoomChangeEvent(List<Connection> roomsConnection) {
        this.roomsConnection = roomsConnection;
    }

    public List<Connection> getRoomsConnection() {
        return roomsConnection;
    }
}
