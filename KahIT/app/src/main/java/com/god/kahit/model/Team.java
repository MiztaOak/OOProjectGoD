package com.god.kahit.model;

import java.util.List;

public class Team {
    private final List<Player> teamMembers;
    private int teamScore;
    private String teamName;


    public Team(List<Player> teamMembers, int teamScore, String teamName) {
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

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
    }


    public  void setTeamName(String teamName){
        this.teamName = teamName;
    }

    public void addPlayer(Player player) {
        teamMembers.add(player);
    }

    public void removePlayer(Player player) {
        teamMembers.remove(player);
    }
}
