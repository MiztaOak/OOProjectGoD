package com.god.kahit;

import android.content.Context;

import com.god.kahit.model.Player;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuizGame;
import com.god.kahit.model.QuizListener;
import com.god.kahit.model.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private QuizGame quizGame;
    private static Repository instance;

    private Repository() {
        //registerOnEventBus();
    }
    public static Repository getInstance() {
        if(instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void startNewGameInstance(Context context){
        quizGame = new QuizGame(context);
    }

    public void addQuizListener(QuizListener quizListener){
        quizGame.addListener(quizListener);
    }

    public void startGame(){
        quizGame.setupGame();
    }

    public void nextQuestion(){
        quizGame.nextQuestion();
    }

    public void sendAnswer(String givenAnswer, Question question, long timeLeft){
        quizGame.receiveAnswer(givenAnswer,question, timeLeft);
    }

    public void registerOnEventBus(){
    //    eventBus.register(this);
    }

    public List<Player> getPlayers(){
        return quizGame.getPlayers();
    }

}
