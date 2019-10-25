package com.god.kahit.applicationEvents;
/**
 * @responsibility: This class is responsible for handling information when joining a lobby.
 * The event is fired when a player has joined a lobby and is not a host.
 * @used-by: This class is used in the following classes:
 * Repository and JoinLobbyNetView
 * @author: Anas Alkoutli
 */

import com.god.kahit.model.IEvent;

public class GameJoinedLobbyEvent implements IEvent {
    public GameJoinedLobbyEvent() {
    }
}
