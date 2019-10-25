package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;
import com.god.kahit.networkManager.Connection;

import java.util.List;

/**
 * Event used to notify a observer that the room connection has changed
 * <p>
 * used by: JoinLobbyViewModel, Repository
 *
 * @author Mats Cedervall
 */
public class RoomChangeEvent implements IEvent {
    private final List<Connection> roomsConnection;

    public RoomChangeEvent(List<Connection> roomsConnection) {
        this.roomsConnection = roomsConnection;
    }

    public List<Connection> getRoomsConnection() {
        return roomsConnection;
    }
}
