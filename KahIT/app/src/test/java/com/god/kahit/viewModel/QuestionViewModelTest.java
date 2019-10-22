package com.god.kahit.viewModel;

import android.animation.ObjectAnimator;
import android.view.View;

import com.god.kahit.model.Category;
import com.god.kahit.model.Question;

import org.junit.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class QuestionViewModelTest {
    private QuestionViewModel q =  mock(QuestionViewModel.class);
    private List<String> alternatives = Arrays.asList("answer 1", "answer 2","answer 3","answer 4");
    private Question questionTest = new Question(Category.Celebrities, "hej?", "answer 2", alternatives, 10 );
    private Question questionTest1 = mock(Question.class);
    private ObjectAnimator objectAnimator = mock(ObjectAnimator.class);

    @Test
    public void isCorrectAnswer() {
        //int answerIndex = q.getCurrentQuestion().getAlternatives().indexOf(q.getCurrentQuestion().getAnswer());
        System.out.println(q.getCurrentQuestion());
        q.onAnswerClicked(1, objectAnimator);
        assertFalse(q.isCorrectAnswer());
    }

    @Test
    public void isAutoAnswer() {
        assertFalse(q.isAutoAnswer());
    }

    @Test
    public void getTwoIndexes() {
        q.setCurrentQuestion(questionTest);
        System.out.println(q.getCurrentQuestion());
    }

    @Test
    public void autoChooseAnswer1() {
        assertEquals(0, q.autoChooseAnswer());
    }
}
