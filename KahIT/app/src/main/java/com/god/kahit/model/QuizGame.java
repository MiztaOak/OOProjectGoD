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
    private final List<Player> users;
    private Map<Category, List<Question>> questionMap;
    private Map<Category, List<Integer>> indexMap;

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
        users = new ArrayList<>();

        listeners = new ArrayList<>();
        currentUser = new Player("local",0,new ArrayList<Item>());

        QuestionFactory.setDataLoader(new QuestionDataLoaderDB(context));
        questionMap = QuestionFactory.getFullQuestionMap();
        indexMap = new HashMap<>();

        store = new Store();
        lottery = new Lottery();

        //setupGame(); //TODO remove this since the method should be called external when the game is started
    }

    /**
     * Method that deals with the setup of a game
     */
    public void setupGame() {
        currentCategory = Category.Mix;
        loadIndexMap();
        startRound();
    }

    private void loadIndexMap() {
        for (Category category : questionMap.keySet()) {
            loadIndexList(category);
        }
    }

    private void loadIndexList(Category category) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < questionMap.get(category).size(); i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        indexMap.put(category, indexes);
    }

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

    private void addQuestion(Category category) {
        if (indexMap.get(category).size() == 0) {
            loadIndexList(category);
        }
        roundQuestions.add(questionMap.get(category).get(indexMap.get(category).get(0))); //TODO make nice
        indexMap.get(category).remove(0);
    }

    public void nextQuestion() {
        if (!roundQuestions.isEmpty()) {
            broadCastQuestion(roundQuestions.pop());
        } else {
            startRound();
        }
    }

    private void broadCastQuestion(final Question question) {
        for (QuizListener quizListener : listeners) {
            quizListener.receiveQuestion(question);
        }
    }

    public void addListener(QuizListener quizListener) {
        listeners.add(quizListener);
    }

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
    public boolean isRoundOver() {
        return roundQuestions.isEmpty();
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }
}
