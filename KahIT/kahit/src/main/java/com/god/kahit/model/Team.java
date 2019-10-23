package com.god.kahit.model;

import java.util.List;

public class Team { //todo use package-private instead of public on many of the methods
    private final List<Player> teamMembers;
    private int teamScore; //todo replace with a method that calc average player score
    private String teamName;
    private final String id;
    //todo add method to set/get teamId

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

    public String getId() {
        return id;
    }
}