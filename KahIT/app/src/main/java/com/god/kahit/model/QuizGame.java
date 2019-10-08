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

public class QuizGame {
    public static final EventBus BUS = new EventBus();

    private static final int MAX_ALLOWED_PLAYERS = 8;
    
    private List<Team> teamList;
    private final List<Player> players;

    private Map<Category, List<Question>> questionMap;
    private Map<Category, List<Integer>> indexMap;
    private Deque<Question> roundQuestions;
    private int numOfQuestions = 3; //TODO replace with more "dynamic" way to set this
    private Category currentCategory;

    private Boolean gameIsStarted = false;

    private List<QuizListener> listeners;
    /**
     * This variable is used to reference to the local user in multiplayer or the current in hotswap
     */
    private Player currentUser;
    private Store store;
    private Lottery lottery;
    private int scorePerQuestion = 100; //TODO replace with a way to calculate a progressive way to calculate the score based on time;

    public QuizGame() {
        players = new ArrayList<>();
        listeners = new ArrayList<>();
        currentUser = new Player("local", 0);
        players.add(currentUser);

        store = new Store();
        lottery = new Lottery();

        initTeamList();
    }

    public void startGame(){
        if(!gameIsStarted){
            questionMap = QuestionFactory.getFullQuestionMap();
            indexMap = new HashMap<>();
            currentCategory = Category.Mix;
            loadIndexMap();

            gameIsStarted = true;
        }
    }

    public void endGame(){
        gameIsStarted = false;
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
        for (int i = 0; i < questionMap.get(category).size(); i++) {
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
            for (int i = 0; i < numOfQuestions; i++) {
                addQuestion(currentCategory);
            }
        } else {
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
    }

    /**
     * Method that adds a question to the round que based on the category given and the order defined
     * in the index map. If there are no more indexes of a given category it refills the index map
     * and then gets a new question based on the new order.
     *
     * @param category the category of the added question
     */
    private void addQuestion(Category category) {
        if (indexMap.get(category).size() == 0) {
            loadIndexList(category);
        }
        roundQuestions.add(questionMap.get(category).get(indexMap.get(category).get(0))); //TODO make nice
        indexMap.get(category).remove(0);
    }

    /**
     * Method that returns a new question if there is questions left in the question que otherwise it
     * starts a new round
     */
    public void nextQuestion() {
        if (!roundQuestions.isEmpty()) {
            broadCastQuestion(roundQuestions.pop());
        } else {
            startRound();
        }
    }

    /**
     * Method that broadcasts the current question to all listeners of the QuizListener interface
     *
     * @param question the question that is being broadcast
     */
    private void broadCastQuestion(final Question question) {
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
    public void receiveAnswer(String givenAnswer, Question question, long timeLeft) {
        if (question.isCorrectAnswer(givenAnswer)) {
            currentUser.updateScore((int)(scorePerQuestion*(timeLeft/question.getTime())));
            //TODO if hotswap change currentUser
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

    public List<Player> getPlayers() {
        return players;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    /**
     * Initiate teams where an integer max allowed players determines how many.
     */
    public void initTeamList() {
        for (int i = 0; i < MAX_ALLOWED_PLAYERS; i++) {
            createNewTeam();
        }
    }

    /**
     * Creates a new empty team with default settings.
     */
    public void createNewTeam() {
        List<Player> players = new ArrayList<>();
        Team team = new Team(players, 0, "Team " + (teamList.size() + 1));
        teamList.add(team);
    }

    /**
     * Creates a new player if less then maximum allowed players and adds it to the first empty team that is found.
     * Then posts the change on the BUS.
     */
    public void addNewPlayerToEmptyTeam() {
        if (getTotalAmountOfPlayers() < MAX_ALLOWED_PLAYERS) {
            for (int i = 0; i < teamList.size(); i++) {
                if (teamList.get(i).getTeamMembers().size() == 0) {
                    teamList.get(i).getTeamMembers().add(createNewPlayer());
                    break;
                }
            }
            //teamList.get(0).getTeamMembers().add(createNewPlayer()); //TODO
            BUS.post(new TeamChangeEvent(teamList));
        }
    }

    /**
     * Removes a specific player from the game and post the change on the BUS.
     *
     * @param player the player to bew removed.
     */
    public void removePlayer(Player player) {
        if (getTotalAmountOfPlayers() > 1) {
            for (int i = 0; i < teamList.size(); i++) {
                teamList.get(i).getTeamMembers().remove(player);
            }
            BUS.post(new TeamChangeEvent(teamList));
        }
    }

    /**
     * Returns a new player with default settings.
     *
     * @return the newly created player.
     */
    public Player createNewPlayer() {
        int i = 1;
        String name = "Player " + (getTotalAmountOfPlayers() + i);
        while (isPlayerNameTaken(name)) {
            name = "Player " + i;
            i++;
        }
        return new Player(name, 0);
    }

    /**
     * Checks if a player name is taken in order to avoid duplicates.
     *
     * @param name the name to be checked.
     * @return True if name is taken.
     */
    private boolean isPlayerNameTaken(String name) {
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.get(i).getTeamMembers().size(); j++) {
                if (teamList.get(i).getTeamMembers().get(j).getName().contentEquals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes a team from the game then posts the change on the BUS.
     *
     * @param team the team to be removed.
     */
    public void removeTeam(Team team) {
        teamList.remove(team);
        BUS.post(new TeamChangeEvent(teamList));
    }


    /**
     * Changes the team name and posts it on the BUS.
     *
     * @param team     team that changes name.
     * @param teamName The team name that the team changes to.
     */
    public void changeTeamName(Team team, String teamName) {
        for (Team team1 : teamList) {
            if (team1.equals(team)) {
                team1.setTeamName(teamName);
            }
        }
        BUS.post(new TeamChangeEvent(teamList));
    }

    /**
     * Changes a players name and posts it on the BUS.
     *
     * @param player the player to have his name changed.
     * @param name   the new name for the player.
     */
    public void changePlayerName(Player player, String name) {
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.get(i).getTeamMembers().size(); j++) {
                if (teamList.get(i).getTeamMembers().get(j).equals(player)) {
                    teamList.get(i).getTeamMembers().get(j).setName(name);
                }
            }
        }
        BUS.post(new TeamChangeEvent(teamList));
    }

    /**
     * Get the total amount of players currently in the game.
     *
     * @return the number of players.
     */
    private int getTotalAmountOfPlayers() {
        int numOfPlayers = 0;
        for (int i = 0; i < teamList.size(); i++) {
            numOfPlayers += getTeamList().get(i).getTeamMembers().size();
        }
        return numOfPlayers;
    }

    /**
     * Resets the entire teamList.
     */
    public void resetPLayerData() {
        teamList.clear();
    }

    /**
     * Changes the team for the player and posts the change on the BUS.
     *
     * @param player     The player that needs to change team.
     * @param newTeamNum The index for the new team.
     */
    public void changeTeam(Player player, int newTeamNum) {
        for (Team team : teamList) {
            team.removePlayer(player);
        }
        teamList.get(newTeamNum).getTeamMembers().add(player);
        BUS.post(new TeamChangeEvent(teamList));
    }

    /**
     * Checks if all players are ready by utilizing the players boolean.
     *
     * @return
     */
    public boolean checkAllPlayersReady() {
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.get(i).getTeamMembers().size(); j++) {
                if (!teamList.get(i).getTeamMembers().get(j).isPlayerReady()) {
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
    public void setIsPlayerReady(Player player, boolean state) {
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.get(i).getTeamMembers().size(); j++) {
                if (teamList.get(i).getTeamMembers().get(j).equals(player)) {
                    teamList.get(i).getTeamMembers().get(j).setPlayerReady(state);
                }
            }
        }
    }
}
