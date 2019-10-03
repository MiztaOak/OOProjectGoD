package com.god.kahit.model;

import com.god.kahit.Events.TeamChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Temporary class but someone introduced context in Quizgame so i'm not going to bother - Jakob
 */
public class Lobby {
    public static final EventBus BUS = new EventBus();
    private final List<Team> teamList;
    private final List<Player> users;

    public Lobby() {
        teamList = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void getNewTeam() {
        List<Player> players = new ArrayList<>();
        Team team = new Team(players, 0, "Team " + (getAmountOfTeams() + 1));
        teamList.add(team);

        BUS.post(new TeamChangeEvent(teamList));
    }

    public void deleteTeam(int teamId) {
        if (teamId > 1) {
            getTeamList().remove(teamId - 1);
            BUS.post(new TeamChangeEvent(teamList));
        }
    }

    /*public void distributePlayersBetweenTeams(int index){
        List<Player> playersToBeMoved = getTeamList().get(index).getTeamMembers();
        int i;
        int j = getTeamList().size();
        for(i = 0; i < getTeamList().size(); i++) {
            if() {
                getTeamList().get(i).addPlayer(playersToBeMoved.get(i));
            }
                getTeamList().get(j).addPlayer(playersToBeMoved.get(i));
            getTeamList().
        }
    }*/


    public void addPlayerToTeam(Player player, int index) {
        if (getAmountOfTeams() < 8 || getTotalAmountOfPlayers() < 8) {
            getNewTeam(); //TODO maybe you don't want to join a new team, maybe you want to join a already existing team.
            getTeamList().get(index).addPlayer(player);
            BUS.post(new TeamChangeEvent(teamList));
        }
    }

    public void removePlayerFromTeam(Player player, int teamId) {
        getTeamList().get(teamId).removePlayer(player);
        BUS.post(new TeamChangeEvent(teamList));
    }


    public Player createNewPlayer() {
        List<Item> buyableItems = new ArrayList<>();
        return new Player("Player " + getTotalAmountOfPlayers(), 0, buyableItems);
    }

    public void deletePlayer(Player player, int index) {
        getTeamList().get(index).removePlayer(player);
    }

    public void changeTeamName(int index, String string) {
        getTeamList().get(index).setTeamName(string);
    }

    public void changePlayerName() {

    }

    private int getTotalAmountOfPlayers() {
        int numOfPlayers = 0;
        for (int i = 0; i < getAmountOfTeams(); i++) {
            numOfPlayers += getTeamList().get(i).getTeamMembers().size();
        }
        return numOfPlayers + 1;
    }

    private int getAmountOfTeams() {
        return teamList.size();
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public List<Player> getUsers() {
        return users;
    }
}
