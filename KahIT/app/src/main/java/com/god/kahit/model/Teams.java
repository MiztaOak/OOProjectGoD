package com.god.kahit.model;

import java.util.List;

public class Teams {
    private final List<Player> teamMembers;
    private int teamScore;
    private int teamName;


    public Teams(List<Player> teamMembers, int teamScore, int teamName) {
        this.teamMembers = teamMembers;
        this.teamScore = teamScore;
        this.teamName = teamName;
    }

    public List<Player> getTeamMembers() {
        return teamMembers;
    }

    public int getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
    }

    public void setTeamName(int teamName) {
        this.teamName = teamName;
    }

    public void addPlayer(Player player) {
        teamMembers.add(player);
    }

    public void removePlayer(Player player) {
        teamMembers.remove(player);
    }
}
