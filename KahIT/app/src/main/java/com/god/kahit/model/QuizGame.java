package com.god.kahit.model;

import android.content.Context;

import com.god.kahit.databaseService.QuestionDataLoaderDB;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizGame {
    private final List<Teams> teams;
    private final List<Player> players;
    private Map<Category,List<Question>> questionMap;
    private Map<Category,List<Integer>> indexMap;

    private Deque<Question> roundQuestions;
    private int numOfQuestions = 3;
    private Category currentCategory;

    private List<QuizListener> listeners;

    /**
     * This variable is used to reference to the local user in multiplayer or the current in hotswap
     */
    private Player currentUser;

    private Store store;
    private Lottery lottery;

    private int scorePerQuestion = 100; //TODO replace with a way to calculate a progressive way to calculate the score based on time;

    public QuizGame(Context context) {
        teams = new ArrayList<>();
        players = new ArrayList<>();
        listeners = new ArrayList<>();
        currentUser = new Player("local",0,new ArrayList<Item>());
        players.add(currentUser);

        QuestionFactory.setDataLoader(new QuestionDataLoaderDB(context));
        questionMap = QuestionFactory.getFullQuestionMap();
        indexMap = new HashMap<>();
        currentCategory = Category.Mix;
        loadIndexMap();

        store = new Store();
        lottery = new Lottery();

        currentCategory = Category.Mix;
        loadIndexMap();
    }

    /**
     * Method that fills the map of questionIndexes that is used to determine the order in which questions
     * are asked
     */
    private void loadIndexMap(){
        for(Category category:questionMap.keySet()){
            loadIndexList(category);
        }
    }

    /**
     * Loads the index list for a single category which is then incorporated into the map
     * @param category - the category serves as the key for the list in the map and is used to get the correct amount of indexes in the list
     */
    private void loadIndexList(Category category){
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
            while (i < numOfQuestions){
                for(Category category: categories){
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
     * @param category the category of the added question
     */
    private void addQuestion(Category category){
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
    public void nextQuestion(){
        if(!roundQuestions.isEmpty()){
            broadCastQuestion(roundQuestions.pop());
        } else {
            startRound();
        }
    }

    /**
     * Method that broadcasts the current question to all listeners of the QuizListener interface
     * @param question the question that is being broadcast
     */
    private void broadCastQuestion(final Question question){
        for(QuizListener quizListener: listeners){
            quizListener.receiveQuestion(question);
        }
    }

    public void addListener(QuizListener quizListener) {
        listeners.add(quizListener);
    }

    /**
     * Method called for the outside of the model to report the given answer on a question
     * @param givenAnswer the alternative that the user choose to provide
     * @param question - the question that was asked
     * @param timeLeft - the time that was left when the user answered the question
     */
    public void receiveAnswer(String givenAnswer,Question question, long timeLeft){
        if(question.isCorrectAnswer(givenAnswer)){
            currentUser.setScore(currentUser.getScore() + scorePerQuestion);
            //TODO if hotswap change currentUser
        }
        //TODO get new question or something
    }

    /**
     * Method that returns true if the round is over
     *
     * @return if the questions stack is empty
     */
    public boolean isRoundOver(){
        if(roundQuestions == null){
            return true;
        }
        return roundQuestions.isEmpty();
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
