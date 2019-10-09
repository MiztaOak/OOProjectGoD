package com.god.kahit;

import android.app.Instrumentation;

import com.god.kahit.databaseService.QuestionDataLoaderRealtime;
import com.god.kahit.model.Category;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuestionFactory;
import com.god.kahit.model.QuizGame;
import com.god.kahit.model.QuizListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class QuizGameTest implements QuizListener {
    private QuizGame quizGame;

    @Before
    public void setUp() throws InterruptedException {
        QuestionFactory.setDataLoader(new QuestionDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getContext()));
        quizGame = new QuizGame();
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

    @Override
    public void receiveQuestion(Question q) {
        Assert.assertEquals(q.getCategory(), Category.History);
    }
}
