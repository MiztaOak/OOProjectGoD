package com.god.kahit.model;

import java.util.List;

/**
 * responsibility: This class is responsible for The teams in the game.
 * It holds a list with all players in the team.
 * When playing in team mode (Not implemented currently) the score will be calculated
 * for the whole team.
 * used-by: This class is used in the following classes:
 * QuizGame, PlayerManager, LobbyNetRecyclerAdapter, LobbyNetView,
 * LobbyNetViewModel, TeamContainerRecyclerAdapter and TeamContainerRecyclerAdapter
 *
 * @author Anas Alkoutli, Johan Ek, Oussama Anadani, Jakob Ewerstrand, Mats Cedervall
 */
public class Team {
    private final List<Player> teamMembers;
    private final String id;
    private String teamName;

    public Team(List<Player> teamMembers, String teamName, String id) {
        this.teamMembers = teamMembers;
        this.teamName = teamName;
        this.id = id;
    }

    public List<Player> getTeamMembers() {
        return teamMembers;
    }

    public String getTeamName() {
        return this.teamName;
    }

    void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    void addPlayer(Player player) {
        teamMembers.add(player);
    }

    void removePlayer(Player player) {
        teamMembers.remove(player);
    }

    public String getId() {
        return id;
    }
}
