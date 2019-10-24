package com.god.kahit.viewModel;

import android.animation.ObjectAnimator;

import android.os.Looper;


import com.god.kahit.Repository.Repository;
import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;
import com.god.kahit.model.Category;
import com.god.kahit.model.Question;
import com.god.kahit.model.modelEvents.QuestionEvent;

import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class QuestionViewModelTest {

    //    private QuestionViewModel q =  mock(QuestionViewModel.class);
    Repository repository = mock(Repository.class);
    //private QuestionViewModel q = new QuestionViewModel(repository);
    private Question questionTest = new Question(Category.Celebrities, "hej?", "answer 2", Arrays.asList("answer 1", "answer 2","answer 3","answer 4"), 10 );

    private QuestionViewModel q = mock(QuestionViewModel.class);
    private List<String> alternatives = Arrays.asList("answer 1", "answer 2", "answer 3", "answer 4");

    private ObjectAnimator objectAnimator = mock(ObjectAnimator.class);
    private QuestionEvent questionEvent = new QuestionEvent(questionTest, 4);
    Looper looper = mock(Looper.class);
    @Test
    public void isCorrectAnswer() {
        /*
        BUS.post(questionEvent);
        int answerIndex = q.getCurrentQuestion().getAlternatives().indexOf(q.getCurrentQuestion().getAnswer());
        System.out.println(answerIndex);
        q.onAnswerClicked(answerIndex, objectAnimator);
        assertTrue(q.isCorrectAnswer());

         */
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
