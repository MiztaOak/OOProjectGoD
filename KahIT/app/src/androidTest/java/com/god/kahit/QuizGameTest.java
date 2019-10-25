package com.god.kahit;

import android.os.Looper;

import androidx.test.platform.app.InstrumentationRegistry;

import com.god.kahit.applicationEvents.EventBusGreenRobot;
import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.databaseService.QuestionDataLoaderRealtime;
import com.god.kahit.model.Category;
import com.god.kahit.model.GameMode;
import com.god.kahit.model.ItemFactory;
import com.god.kahit.model.PlayerManager;
import com.god.kahit.model.QuestionFactory;
import com.god.kahit.model.QuizGame;
import com.god.kahit.model.modelEvents.QuestionEvent;

import org.greenrobot.eventbus.Subscribe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class QuizGameTest {
    private QuizGame quizGame;

    @Before
    public void setUp() throws InterruptedException {
        try {
            QuestionFactory.setDataLoader(new QuestionDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getTargetContext()));
            ItemFactory.setDataLoader(new ItemDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getTargetContext()));
        } catch (Exception e) {
            Looper.prepare();
            QuestionFactory.setDataLoader(new QuestionDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getTargetContext()));
            ItemFactory.setDataLoader(new ItemDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getTargetContext()));
        }

        quizGame = new QuizGame(new EventBusGreenRobot(), new PlayerManager(new EventBusGreenRobot(), GameMode.HOT_SWAP), GameMode.HOT_SWAP);
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testStartRound() {
        quizGame.startGame();
        quizGame.setCurrentCategory(Category.History);
        quizGame.startRound();
        do {
            quizGame.nextQuestion();
        } while (!quizGame.isRoundOver());
    }

    @Subscribe
    public void receiveQuestion(QuestionEvent q) {
        Assert.assertEquals(q.getQuestion().getCategory(), Category.History);
    }
}
