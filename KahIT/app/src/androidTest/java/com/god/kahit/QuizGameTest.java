package com.god.kahit;

import android.support.test.InstrumentationRegistry;

import com.god.kahit.model.Category;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuizGame;
import com.god.kahit.model.QuizListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuizGameTest implements QuizListener {
    private QuizGame quizGame;

    @Before
    public void setQuizGame(){
        quizGame = new QuizGame(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @Test
    public void testStartRound(){
        quizGame.setCurrentCategory(Category.History);
        quizGame.startRound();
        do {
            quizGame.nextQuestion();
        }while (!quizGame.isRoundOver());
    }

    @Override
    public void receiveQuestion(Question q) {
        Assert.assertEquals(q.getCategory(), Category.History);
    }
}
