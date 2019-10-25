package com.god.kahit.applicationEvents;
/**
 * This class is responsible for handling information when joining a lobby.
 * The event is fired when a player has joined a lobby and is not a host.
 *
 * used-by: This class is used in the following classes:
 * Repository and JoinLobbyNetView
 *
 * @author: Mats Cedervall
 */

import com.god.kahit.model.IEvent;

public class GameJoinedLobbyEvent implements IEvent {
    public GameJoinedLobbyEvent() {
    }
}
