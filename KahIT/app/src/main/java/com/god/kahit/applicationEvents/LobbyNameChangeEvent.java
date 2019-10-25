package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

/**
 * Event used to notify a observer that the name of the lobby has changed.
 *
 * used by: LobbyNewViewModel, Repository
 *
 * @author Johan Ek
 */
public class LobbyNameChangeEvent implements IEvent {
    private String lobbyName;

    public LobbyNameChangeEvent(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }
}
