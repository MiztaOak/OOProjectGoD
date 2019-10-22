package com.god.kahit.model;

import com.god.kahit.Events.TeamChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerManager {
    private static final int MAX_ALLOWED_PLAYERS = 8;
    private final ArrayList<Team> teamList;
    private final List<Player> playerList;
    private IEventBus IEventBus;
    private GameMode gameMode;
    private Player currentHotSwapPlayer;
    private String localPlayerId;

    public PlayerManager(IEventBus IEventBus, GameMode gameMode) {
        this.IEventBus = IEventBus;
        this.gameMode = gameMode;

        playerList = new ArrayList<>();
        teamList = new ArrayList<>();
        localPlayerId = "iLocal";
    }

    public void addNewPlayerToTeam(String playerName, String playerId, boolean readyStatus, String teamId) {
        Team team = getTeam(teamId);
        if (team == null) {
            createNewTeam(teamId);
        }

        team = getTeam(teamId);
        if (team != null && playerList.size() < MAX_ALLOWED_PLAYERS) {
            Player player = createNewPlayer(playerName, playerId);
            player.setReady(readyStatus);
            playerList.add(player);
            team.addPlayer(player);
            fireTeamChangeEvent();
        }
    }

    /**
     * Checks if all players are ready by utilizing the players boolean.
     *
     * @return
     */
    public boolean checkAllPlayersReady() {
        for (Player player : playerList) {
            if (!player.isReady()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the total amount of players currently in the game.
     *
     * @return the number of players.
     */
    int getTotalAmountOfPlayers() {
        return playerList.size();
    }

    public Player getPlayer(String playerId) {
        for (Player player : playerList) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }

    public Player getLocalPlayer() {
        return getPlayer(localPlayerId);
    }

    public String getLocalPlayerId() {
        return localPlayerId;
    }

    public void setLocalPlayerId(String localPlayerId) {
        this.localPlayerId = localPlayerId;
    }

    public Player getCurrentPlayer() {
        if (gameMode.equals(GameMode.HOT_SWAP)) {
            return currentHotSwapPlayer;
        } else {
            return getPlayer(localPlayerId);
        }
    }

    public Team getPlayerTeam(String playerId) {
        for (Team team : teamList) {
            for (Player teamMember : team.getTeamMembers()) {
                if (teamMember.getId().equals(playerId)) {
                    return team;
                }
            }
        }
        return null;
    }

    public List<Player> getPlayers() {
        return playerList;
    }

    public List<Team> getTeams() {
        return teamList;
    }

    /**
     * Method takes a map of Players as keys and Items as values and applies each values on each key eg. Player gets Item(value) applied.
     *
     * @param winnings a map with winnings.
     */
    void applyModifiers(Map<Player, Item> winnings) {
        for (Player player : getPlayers()) {
            if (winnings.get(player) instanceof Buff) {
                player.setBuff((Buff) Objects.requireNonNull(winnings.get(player)));
            } else if (winnings.get(player) instanceof Debuff) {
                player.setDebuff((Debuff) Objects.requireNonNull(winnings.get(player)));
            } else {
                player.setVanityItem((VanityItem) Objects.requireNonNull(winnings.get(player)));
            }
        }
    }

    public void incrementCurrentPlayer() {
        if (playerList.indexOf(currentHotSwapPlayer) + 1 < playerList.size()) {
            currentHotSwapPlayer = playerList.get(playerList.indexOf(currentHotSwapPlayer) + 1);
        } else {
            currentHotSwapPlayer = playerList.get(0);
        }
    }

    /**
     * Resets the entire teams.
     */
    public void resetPlayerData() {
        teamList.clear();
        playerList.clear();
        currentHotSwapPlayer = null;
    }

    public void resetPlayerReady() {
        for (Player player : playerList) {
            player.setReady(false);
        }
    }

    public void changeTeam(Player player, String newTeamId) {
        System.out.println("QuizGame - changeTeam: Triggered!");

        //Remove player from any other team
        for (int i = teamList.size() - 1; i >= 0; i--) {
            teamList.get(i).removePlayer(player);
        }

        //Handle team exist
        Team team = getTeam(newTeamId);
        if (team != null) {
            team.addPlayer(player);
            fireTeamChangeEvent();
            return;
        }

        //Handle team does not exist
        createNewTeam(newTeamId);
        team = getTeam(newTeamId);
        if (team != null) {
            team.addPlayer(player);
            fireTeamChangeEvent();
        }
    }

    /**
     * Checks if a empty team exists, if not it creates one if the total number of teams are below specified Integer.
     * A new player is then created with the use of the parameters if less then specified integer and adds it to the first empty team that is found.
     * Said player is also added to the playerList.
     * Method then posts the change on the BUS with a teamChangeEvent.
     *
     * @param name for the new player.
     * @param id   for the new player.
     */
    public void addNewPlayerToEmptyTeam(String name, String id) {
        if (noEmptyTeamExists() && teamList.size() < MAX_ALLOWED_PLAYERS) {
            createNewTeam(teamList.size());
        }
        if (playerList.size() < MAX_ALLOWED_PLAYERS) {
            Player player = createNewPlayer(name, id);
            playerList.add(player);
            getEmptyTeam().addPlayer(player);
            fireTeamChangeEvent();
        }
    }


    /**
     * Checks if a empty team exists, if not it creates one if the total number of teams are below specified Integer.
     * A new player is then created with default settings if less then specified maximum allowed players and adds it to the first empty team that is found.
     * Said player is also added to the playerList.
     * Method then posts the change on the BUS with a teamChangeEvent.
     */
    public void addNewPlayerToEmptyTeam() {
        if (noEmptyTeamExists() && teamList.size() < MAX_ALLOWED_PLAYERS) {
            createNewTeam(teamList.size());
        }
        if (playerList.size() < MAX_ALLOWED_PLAYERS) {
            Player player = createNewPlayer();
            getEmptyTeam().addPlayer(player);
            playerList.add(player);
            fireTeamChangeEvent();
        }

    }

    /**
     * Checks if there is an empty team.
     *
     * @return true if empty team, false if there is no empty team.
     */
    public boolean noEmptyTeamExists() {
        for (Team team : teamList) {
            if (team.getTeamMembers().size() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets an empty team from the teamList.
     *
     * @return the empty team.
     */
    public Team getEmptyTeam() {
        int j = 0;
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).getTeamMembers().size() == 0) {
                return teamList.get(i);
            }
        }
        return teamList.get(j);
    }

    /**
     * Creates a new empty team with default settings.
     */
    public void createNewTeam(int teamNumber) {
        List<Player> players = new ArrayList<>();
        String teamName = "Team " + (teamNumber + 1);
        String id = Integer.toString(teamNumber + 1);
        Team team = new Team(players, teamName, id);
        teamList.add(team);
    }

    /**
     * Creates a new empty team with specified id.
     *
     * @param id for the team.
     */
    public void createNewTeam(String id) {
        List<Player> players = new ArrayList<>();
        String teamName = "Team " + id;
        Team team = new Team(players, teamName, id);
        teamList.add(team);
    }

    /**
     * If hotSwap, checks if name is empty and generates a new name until no other player has the same name.
     * Then it returns a player with specified name and id.
     * In the event of a name being used by another player it returns a player with a generated name.
     *
     * @param name the name for the player.
     * @param id   the id for the player.
     * @return
     */
    private Player createNewPlayer(String name, String id) {
        int i = 1;
        if (gameMode.equals(GameMode.HOT_SWAP)) {
            while (isPlayerNameTaken(name)) {
                name = "Player " + i++;
            }
        }

        Player newPlayer = new Player(name, id);
        if (currentHotSwapPlayer == null && playerList.size() > 0) {
            currentHotSwapPlayer = playerList.get(0);
        }

        return newPlayer;
    }

    /**
     * Returns a new player with default name and id.
     *
     * @return a new Player object.
     */
    private Player createNewPlayer() {
        String name = getNewPlayerName();
        String id = name;

        return createNewPlayer(name, id);
    }


    /**
     * Creates a new player name.
     *
     * @return new name.
     */
    private String getNewPlayerName() {
        String namePrefix = "Player ";
        int i = 1;
        while (isPlayerNameTaken(namePrefix + i)) {
            i++;
        }
        return namePrefix + i;
    }

    /**
     * Checks if a player name is taken in order to avoid duplicates.
     *
     * @param name the name to be checked.
     * @return True if name is taken.
     */
    private boolean isPlayerNameTaken(String name) {
        for (Player player : playerList) {
            if (player.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a specific player from the game and post the change on the BUS.
     *
     * @param player the player to bew removed.
     */
    public void removePlayer(Player player) {
        if (playerList.size() > 1) { //TODO should we not instead check if player is current player, or is it wrong to have no players
            for (int i = 0; i < teamList.size(); i++) {
                teamList.get(i).getTeamMembers().remove(player);
            }
            playerList.remove(player);

            fireTeamChangeEvent();
        }
    }

    /**
     * Removes a team from the game.
     *
     * @param team the team to be removed.
     */
    public void removeTeamIfEmpty(Team team) {
        if (team.getTeamMembers().size() == 0) {
            teamList.remove(team);
        }
    }

    /**
     * Changes the team name and posts it on the BUS.
     *
     * @param team     team that changes name.
     * @param teamName The team name that the team changes to.
     */
    public void changeTeamName(Team team, String teamName) {
        int index = teamList.indexOf(team);
        if (index >= 0) {
            teamList.get(index).setTeamName(teamName);
        }
        fireTeamChangeEvent();
    }

    /**
     * Changes a players name and posts it on the BUS.
     *
     * @param player the player to have his name changed.
     * @param name   the new name for the player.
     */
    public void changePlayerName(Player player, String name) {
        if (!player.isReady()) {
            for (Player player1 : playerList) {
                if (player1.equals(player)) {
                    player.setName(name);
                }
            }
            fireTeamChangeEvent();
        }
    }

    /**
     * Checks if the newTeamNum exists if not it creates a new team.
     * Changes the team for the player and posts the change on the BUS.
     *
     * @param player     The player that needs to change team.
     * @param newTeamNum The index for the new team.
     */
    public void changeTeam(Player player, int newTeamNum) {

        for (Team team : teamList) {
            team.removePlayer(player);
        }

        try {
            teamList.get(newTeamNum);
        } catch (IndexOutOfBoundsException e) {
            createNewTeam(newTeamNum);
        }

        teamList.get(newTeamNum).getTeamMembers().add(player);
        fireTeamChangeEvent();
    }

    /**
     * fires a teamChangeEvent.
     */
    public void fireTeamChangeEvent() {
        IEventBus.post(new TeamChangeEvent(teamList));
    }

    private Team getTeam(String teamId) {
        for (Team team : teamList) {
            if (team.getId().equals(teamId)) {
                return team;
            }
        }
        return null;
    }
}
