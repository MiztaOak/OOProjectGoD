package com.god.kahit.model;

import android.content.Context;

import com.god.kahit.databaseService.QuestionDataLoaderDB;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class QuizGame {
    private final List<Teams> teams;
    private final List<User> users;
    private Map<Category,List<Question>> questionMap;

    private Deque<Question> roundQuestions;
    private int numOfQuestions;
    private Category currentCategory;

    private List<QuizListener> listeners;

    /**
     * This variable is used to reference to the local user in multiplayer or the current in hotswap
     */
    private User currentUser;

    private Store store;
    private Lottery lottery;

    private int scorePerQuestion = 100; //TODO replace with a way to calculate a progressive way to calculate the score based on time;

    public QuizGame(Context context) {
        teams = new ArrayList<>();
        users = new ArrayList<>();

        listeners = new ArrayList<>();

        QuestionFactory.setDataLoader(new QuestionDataLoaderDB(context));
        questionMap = QuestionFactory.getFullQuestionMap();

        store = new Store();
        lottery = new Lottery();
    }

    /**
     * Method that deals with the setup of a game
     */
    public void setupGame(){
        currentCategory = Category.Mix;
        startRound();
    }

    public void startRound(){
        roundQuestions = new ArrayDeque<>();

    }

    public void nextQuestion(){
        if(!roundQuestions.isEmpty()){
            broadCastQuestion(roundQuestions.pop());
        }else{
            startRound();
        }
    }

    private void broadCastQuestion(final Question question){
        for(QuizListener quizListener: listeners){
            quizListener.receiveQuestion(question);
        }
    }

    public void addListener(QuizListener quizListener){
        listeners.add(quizListener);
    }

    public void receiveAnswer(String givenAnswer,Question question){
        if(question.isCorrectAnswer(givenAnswer)){
            currentUser.setScore(currentUser.getScore() + scorePerQuestion);
            //TODO if hotswap change currentUser
        }
        //TODO get new question or something
    }

    /**
     * Method that returns true if the round is over
     * @return if the questions stack is empty
     */
    public boolean isRoundOver(){
        return roundQuestions.isEmpty();
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }
}
