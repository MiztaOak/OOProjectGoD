package com.god.kahit.model;

import java.util.List;

public class Team {
    private final List<Player> teamMembers;
    private int teamScore;
    private String teamName;
    private final String id;


    public Team(List<Player> teamMembers, String teamName, String id) {
        this.teamMembers = teamMembers;
        this.teamScore = 0;
        this.teamName = teamName;
        this.id = id;
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

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void addPlayer(Player player) {
        teamMembers.add(player);
    }

    public void removePlayer(Player player) {
        teamMembers.remove(player);
    }
}
