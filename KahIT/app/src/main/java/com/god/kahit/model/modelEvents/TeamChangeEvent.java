package com.god.kahit.model.modelEvents;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.Team;

import java.util.List;

/**
 * A event used to notify a observer when the team list in playerManager has changed
 *
 * used by: HotSwapAddPlayersViewModel, LobbyNetView, LobbyNetViewModel, PlayerManager
 *
 * @author Jakob Ewerstrand & Mats Cedervall
 */
public class TeamChangeEvent implements IEvent {

    private final List<Team> team;

    public TeamChangeEvent(List<Team> team) {
        this.team = team;
    }

    public List<Team> getTeams() {
        return team;
    }

}

