package com.god.kahit.model;

import com.god.kahit.Events.TeamChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuizGame {
    public static final EventBus BUS = new EventBus(); //todo Evaluate if we need this external dependency, instead of making our own observer-pattern
    private static final int MAX_ALLOWED_PLAYERS = 8;
    private final ArrayList<Team> teamList;
    private final List<Player> playerList;
    private Map<Category, List<Question>> questionMap;
    private Map<Category, List<Integer>> indexMap;
    private Deque<Question> roundQuestions;
    private int numOfQuestions = 3; //TODO replace with more "dynamic" way to set this
    private Category currentCategory;
    private Store store; //TODO should this be here??

    //TODO maybe move into constructor
    private String hostPlayerId = "iHost";
    private Boolean gameIsStarted = false;

    private List<QuizListener> listeners;
    /**
     * This variable is used to reference to the local user in multiplayer or the current in hotswap
     */
    private Player currentPlayer; //TODO add method that moves current user through the list of users
    private Lottery lottery;
    private int scorePerQuestion = 100; //TODO replace with a way to calculate a progressive way to calculate the score based on time;

    public QuizGame() {
        teamList = new ArrayList<>(MAX_ALLOWED_PLAYERS);
        playerList = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public void startGame() {
        if (!gameIsStarted) {
            questionMap = QuestionFactory.getFullQuestionMap();
            indexMap = new HashMap<>();
            currentCategory = Category.Mix;
            loadIndexMap();
            store = new Store();
            gameIsStarted = true;
        }
    }

    public void endGame() {
        gameIsStarted = false;
    }

    public boolean hasGameStarted() {
        return gameIsStarted;
    }

    /**
     * Method that fills the map of questionIndexes that is used to determine the order in which questions
     * are asked
     */
    private void loadIndexMap() {
        for (Category category : questionMap.keySet()) {
            loadIndexList(category);
        }
    }

    /**
     * Loads the index list for a single category which is then incorporated into the map
     *
     * @param category - the category serves as the key for the list in the map and is used to get the correct amount of indexes in the list
     */
    private void loadIndexList(Category category) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(questionMap.get(category)).size(); i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        indexMap.put(category, indexes);
    }

    /**
     * Method that start a round of the game. It loads the questions that will be asked during the round
     * based on the current category
     */
    public void startRound() {
        roundQuestions = new ArrayDeque<>();
        if (currentCategory != Category.Mix) {
            loadRoundQuestion();
        } else {
            loadMixQuestions();
        }
    }

    /**
     * Method that loads questions from all categories and puts them into the que
     */
    private void loadMixQuestions() {
        int i = 0;
        List<Category> categories = new ArrayList<>(questionMap.keySet());
        Collections.shuffle(categories);
        while (i < numOfQuestions) {
            for (Category category : categories) {
                addQuestion(category);
                i++;
                if (i == numOfQuestions) {
                    break;
                }
            }
        }
    }

    /**
     * Method that load questions into the que based on the current category
     */
    private void loadRoundQuestion() {
        for (int i = 0; i < numOfQuestions; i++) {
            addQuestion(currentCategory);
        }
    }

    /**
     * Method that adds a question to the round que based on the category given and the order defined
     * in the index map. If there are no more indexes of a given category it refills the index map
     * and then gets a new question based on the new order.
     *
     * @param category the category of the added question
     */
    private void addQuestion(Category category) {
        if (Objects.requireNonNull(indexMap.get(category)).size() == 0) {
            loadIndexList(category);
        }
        roundQuestions.add(Objects.requireNonNull(questionMap.get(category))
                .get(Objects.requireNonNull(indexMap.get(category)).get(0))); //TODO make nice
        Objects.requireNonNull(indexMap.get(category)).remove(0);
    }

    /**
     * Method that returns a new question if there is questions left in the question que otherwise it
     * starts a new round
     */
    public void nextQuestion() {
        if (!roundQuestions.isEmpty()) {
            broadCastQuestion(roundQuestions.pop());
        } else {
            startRound(); //TODO is this expected?
        }
    }

    public Question getNextQuestion() {
        if (!roundQuestions.isEmpty()) {
            return (roundQuestions.peek());
        } else {
            startRound(); //TODO is this expected?
            return roundQuestions.peek();
        }
    }

    /**
     * Method that broadcasts the current question to all listeners of the QuizListener interface
     *
     * @param question the question that is being broadcast
     */
    private void broadCastQuestion(Question question) {
        for (QuizListener quizListener : listeners) {
            quizListener.receiveQuestion(question);
        }
    }

    public void addListener(QuizListener quizListener) {
        listeners.add(quizListener);
    }

    /**
     * Method called for the outside of the model to report the given answer on a question
     *
     * @param givenAnswer the alternative that the user choose to provide
     * @param question    - the question that was asked
     * @param timeLeft    - the time that was left when the user answered the question
     */
    public void enterAnswer(Player player, String givenAnswer, Question question, long timeLeft) {
        if (question.isCorrectAnswer(givenAnswer)) {
            double scoreDelta = ((double) scorePerQuestion) * (((double) timeLeft) / ((double) question.getTime()));
            player.updateScore((int) scoreDelta);
        }
    }

    /**
     * Method that returns true if the round is over
     *
     * @return if the questions stack is empty
     */
    public boolean isRoundOver() {
        if (roundQuestions == null) {
            return true;
        }
        return roundQuestions.isEmpty();
    }

    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public Player getPlayer(String playerId) {
        for (Player player : playerList) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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

    private Team getTeam(String teamId) {
        for (Team team : teamList) {
            if (team.getId().equals(teamId)) {
                return team;
            }
        }
        return null;
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
        if (getTotalAmountOfPlayers() < MAX_ALLOWED_PLAYERS) {
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
     * Checks if name is empty and generates a new name until no other player has the same name.
     * Then it returns a player with specified name and id.
     * In the event of a name being used by another player it returns a player with a generated name.
     *
     * @param name the name for the player.
     * @param id   the id for the player.
     * @return
     */
    public Player createNewPlayer(String name, String id) {
        int i = playerList.size();
        while (isPlayerNameTaken(name)) {
            name = "Player " + i;
            i++;
        }
        return new Player(name, id);
    }

    /**
     * Returns a new player with default name and id.
     *
     * @return a new Player object.
     */
    public Player createNewPlayer() {
        String name = getNewPlayerName();
        String id = name;

        return new Player(name, id);
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
        if (getTotalAmountOfPlayers() > 1) { //TODO should we not instead check if player is current player, or is it wrong to have no players
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
     * Get the total amount of players currently in the game.
     *
     * @return the number of players.
     */
    public int getTotalAmountOfPlayers() {
        return playerList.size();
    }

    /**
     * Resets the entire teams.
     */
    public void resetPlayerData() {
        teamList.clear();
        playerList.clear();
        currentPlayer = null;
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
        BUS.post(new TeamChangeEvent(teamList));
    }


    /**
     * Checks if all players are ready by utilizing the players boolean.
     *
     * @return
     */
    public boolean checkAllPlayersReady() { //todo remove ready-related stuff from player, move responsibility to viewModel
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.get(i).getTeamMembers().size(); j++) {
                if (!teamList.get(i).getTeamMembers().get(j).isReady()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * sets the players boolean.
     *
     * @param player the affected player.
     * @param state  the boolean value to be set.
     */
    public void setIsPlayerReady(Player player, boolean state) {  //todo remove ready-related stuff from player, move responsibility to viewModel
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.get(i).getTeamMembers().size(); j++) {
                if (teamList.get(i).getTeamMembers().get(j).equals(player)) {
                    teamList.get(i).getTeamMembers().get(j).setReady(state);
                    break;
                }
            }
        }
        fireTeamChangeEvent();
    }

    /**
     * Gets a Map with a Player as key and a won item as value.
     *
     * @return Map with players and Items.
     */
    public Map<Player, Item> getWinnings() {
        Map<Player, Item> winnings = lottery.drawItem(playerList);
        applyModifiers(winnings);
        return winnings;
    }

    /**
     * Method takes a map of Players as keys and Items as values and applies each values on each key eg. Player gets Item(value) applied.
     *
     * @param winnings a map with winnings.
     */
    private void applyModifiers(Map<Player, Item> winnings) {
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

    /**
     * Applies a single modifier on a single player.
     *
     * @param player to have a modifier applied to.
     * @param item   to be applied.
     */
    //TODO is this method needed. isn't it the same as the previous one?
    public void applyModifier(Player player, Item item) {
        if (item instanceof Buff) {
            player.setBuff((Buff) item);
        } else if (item instanceof Debuff) {
            player.setDebuff((Debuff) item);
        } else {
            player.setVanityItem((VanityItem) item);
        }
    }

    /**
     * Method returns all items available in the game.
     *
     * @return
     */
    public List<Item> getAllItems
    () {
        return lottery.getItemList();
    }

    public String getHostPlayerId() {
        return hostPlayerId;
    }

    public void setHostPlayerId(String hostPlayerId) {
        this.hostPlayerId = hostPlayerId;
    }

    public Store getStore() {
        if (store == null) {
            store = new Store();
        }
        return store;
    }
}
