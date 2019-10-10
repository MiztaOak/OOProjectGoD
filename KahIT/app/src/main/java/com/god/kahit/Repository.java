package com.god.kahit;

import android.content.Context;

import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.databaseService.QuestionDataLoaderRealtime;
import com.god.kahit.model.Category;
import com.god.kahit.model.Item;
import com.god.kahit.model.ItemFactory;
import com.god.kahit.model.Player;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuestionFactory;
import com.god.kahit.model.QuizGame;
import com.god.kahit.model.QuizListener;

import java.util.List;
import java.util.Map;

public class Repository {

    private static Repository instance;
    private QuizGame quizGame;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void startNewGameInstance(Context context) {
        QuestionFactory.setDataLoader(new QuestionDataLoaderRealtime(context));
        ItemFactory.setDataLoader(new ItemDataLoaderRealtime(context));
        quizGame = new QuizGame();
    }

    public void addQuizListener(QuizListener quizListener) {
        quizGame.addListener(quizListener);
    }

    public void startGame() {
        quizGame.startGame();
        quizGame.startRound();
    }


    public void nextQuestion() {
        quizGame.nextQuestion();
    }

    public void sendAnswer(String givenAnswer, Question question, long timeLeft) {
        quizGame.enterAnswer(givenAnswer, question, timeLeft);
    }

    public List<Player> getPlayers() {
        return quizGame.getPlayers();
    }

    public boolean isRoundOver() {
        return quizGame.isRoundOver();
    }

    public Category getCurrentCategory() {
        return quizGame.getCurrentCategory();
    }

    public void setCurrentCategory(Category currentCategory) {
        quizGame.setCurrentCategory(currentCategory);
    }

    public void resetPlayerData() {
        quizGame.resetPLayerData();
    }

    public void addNewPlayer() {
        quizGame.createNewPlayer();
    }

    public void addNewPlayerToEmptyTeam() {
        quizGame.addNewPlayerToEmptyTeam();
    }

    public void fireTeamChangeEvent() {
        quizGame.fireTeamChangeEvent();
    }

    public void removePlayer(Player player) {
        quizGame.removePlayer(player);
    }

    public void changeTeam(Player player, int newTeamId) {
        quizGame.changeTeam(player, newTeamId);
    }

    public Map<Player, Item> getDrawResult() {
        return quizGame.getWinnings();
    }

    public List<Item> getAllItems() {
        return quizGame.getAllItems();
    }

}
