package com.god.kahit;

import com.god.kahit.model.QuizGame;

public class Repository {

    private QuizGame quizGame;
    private static Repository instance;


    private Repository() {
        registerOnEventBus();
    }
    public static Repository getInstance() {
        if(instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void registerOnEventBus(){
    //    eventBus.register(this);
    }

}
