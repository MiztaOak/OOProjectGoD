package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class Teams {
    private final List<User> teamMembers;
    private int teamScore;

    public Teams(List<User> teamMembers, int teamScore) {
        this.teamMembers = teamMembers;
        this.teamScore = teamScore;
    }

    public List<User> getTeamMembers() {
        return teamMembers;
    }

    public int getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
    }

    boolean isPartOfTeam(User user){
        return teamMembers.contains(user);
    }
}
