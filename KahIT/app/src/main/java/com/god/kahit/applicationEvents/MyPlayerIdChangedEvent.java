package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

/**
 * Event used to notify a observer that the local uses id changed, used to update liveData in the
 * viewModel layer.
 *
 * used-by: LobbyViewModel, Repository
 *
 * @author Mats Cedervall
 */
public class MyPlayerIdChangedEvent implements IEvent {
    private String newPlayerId;

    public MyPlayerIdChangedEvent(String newPlayerId) {
        this.newPlayerId = newPlayerId;
    }

    public String getNewPlayerId() {
        return newPlayerId;
    }
}
