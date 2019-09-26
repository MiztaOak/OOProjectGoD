package com.god.kahit;

import com.god.kahit.model.QuizGame;

import org.greenrobot.eventbus.EventBus;

public class Repository {

    private QuizGame quizGame;
    private static Repository instance;
    EventBus eventBus = EventBus.getDefault();

    private Repository() {
        quizGame = new QuizGame();
        registerOnEventBus();
    }
    public static Repository getInstance() {
        if(instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void registerOnEventBus(){
        eventBus.register(this);
    }

}
