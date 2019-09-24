package com.god.kahit.model;

import android.content.Context;

import com.god.kahit.databaseService.QuestionDataLoaderDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizGame {
    private final List<Teams> teams;
    private final List<User> users;
    private Map<Category,List<Question>> questionMap;

    /**
     * This variable is used to reference to the local user in multiplayer or the current in hotswap
     */
    private User currentUser;

    private Store store;
    private Lottery lottery;

    public QuizGame(Context context) {
        teams = new ArrayList<>();
        users = new ArrayList<>();

        QuestionFactory.setDataLoader(new QuestionDataLoaderDB(context));
        questionMap = QuestionFactory.getFullQuestionMap();

        store = new Store();
        lottery = new Lottery();
    }

    /**
     * Method that deals with the setup of a game
     */
    public void setupGame(){

    }

    /**
     * Method that contains the main loop of the game i guess
     */
    private void game(){

    }

    private void round(Category category, int numQuestions){
        
    }

    /**
     * Method that starts the game if it is not started already
     */
    public void startGame(){

    }
}
