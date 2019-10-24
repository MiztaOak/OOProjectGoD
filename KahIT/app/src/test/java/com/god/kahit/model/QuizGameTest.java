package com.god.kahit.model;

import com.god.kahit.ItemDataLoaderMock;
import com.god.kahit.QuestionDataLoaderMock;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class QuizGameTest {
    QuizGame quizGame;

    @Before
    public void setUp() throws Exception {
        ItemFactory.setDataLoader(new ItemDataLoaderMock());
        QuestionFactory.setDataLoader(new QuestionDataLoaderMock());
        //IEventBus eventBus, PlayerManager playerManager, GameMode gameMode
        IEventBus bus = mock(IEventBus.class);
        PlayerManager playerManager = mock(PlayerManager.class);
        quizGame = new QuizGame(bus, playerManager, GameMode.HOT_SWAP);
    }

    @Test
    public void startGame() {
        quizGame.startGame();
        assertTrue(quizGame.isGameIsStarted());
        assertNotNull(quizGame.getLottery());
        assertNotNull(quizGame.getStore());
        assertEquals(Category.Mix, quizGame.getCurrentCategory());
    }
/*
*
*
* */

    @Test
    public void endGame() {
    }

    @Test
    public void startRound() {
    }

    @Test
    public void nextQuestion() {
    }

    @Test
    public void enterAnswer() {
    }

    @Test
    public void isRoundOver() {
    }

    @Test
    public void addNewPlayerToTeam() {
    }

    @Test
    public void addNewPlayerToEmptyTeam() {
    }

    @Test
    public void addNewPlayerToEmptyTeam1() {
    }

    @Test
    public void noEmptyTeamExists() {
    }

    @Test
    public void getEmptyTeam() {
    }

    @Test
    public void createNewTeam() {
    }

    @Test
    public void createNewTeam1() {
    }

    @Test
    public void createNewPlayer() {
    }

    @Test
    public void createNewPlayer1() {
    }

    @Test
    public void removePlayer() {
    }

    @Test
    public void removeTeamIfEmpty() {
    }

    @Test
    public void changeTeamName() {
    }

    @Test
    public void changePlayerName() {
    }

    @Test
    public void getTotalAmountOfPlayers() {
    }

    @Test
    public void resetPlayerData() {
    }

    @Test
    public void changeTeam() {
    }

    @Test
    public void changeTeam1() {
    }

    @Test
    public void checkAllPlayersReady() {
    }

    @Test
    public void setIsPlayerReady() {
    }

    @Test
    public void applyModifier() {
    }

    @Test
    public void getAllItems() {
        //assertTrue(quizGame.getAllItems().size()==2);
    }

    @Test
    public void incrementCurrentPlayer() {
    }
}