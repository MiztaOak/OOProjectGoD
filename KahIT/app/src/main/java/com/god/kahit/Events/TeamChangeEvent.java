package com.god.kahit.Events;

import com.god.kahit.model.Team;

import java.util.List;

public class TeamChangeEvent {

    private final List<Team> team;

    public TeamChangeEvent(List<Team> team) {
        this.team = team;
    }

    public List<Team> getTeam() {
        return team;
    }

}

