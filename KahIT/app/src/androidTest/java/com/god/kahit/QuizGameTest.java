package com.god.kahit;

import com.god.kahit.model.Category;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuizGame;
import com.god.kahit.model.QuizListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class QuizGameTest implements QuizListener {
    private QuizGame quizGame;

    @Before
    public void setQuizGame() {
        quizGame = new QuizGame(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @Test
    public void testStartRound() {
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
