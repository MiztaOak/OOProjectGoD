package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

/**
 * Event used to notify a observer that the game has started
 * <p>
 * used by: LobbyNetView, Repository
 *
 * @author Mats Cedervall
 */
public class GameStartedEvent implements IEvent {
    public GameStartedEvent() {
    }
}
