package com.god.kahit.viewModel;

import android.os.Looper;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Category;
import com.god.kahit.model.Question;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import androidx.test.platform.app.InstrumentationRegistry;

public class QuestionViewModelTest {
    private QuestionViewModel questionViewModel;
    private Question question;

    @Before
    public void setup(){
        Looper.prepare();
        Repository.getInstance().startNewGameInstance(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Repository.getInstance().setupAppLifecycleObserver(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Repository.getInstance().setupAudioHandler(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Repository.getInstance().resetApp();

        questionViewModel = new QuestionViewModel();
        List<String> qAlts = Arrays.asList("TestAns", "Test1", "Test2", "Test3");
        question = new Question(Category.Test,"Test","TestAns",qAlts,10);
        questionViewModel.getQuestionAlts();
        questionViewModel.getQuestionText();
        questionViewModel.getQuestionTime();
        questionViewModel.getPlayerName();
    }

    @Test
    public void receiveQuestionTest(){
        questionViewModel.receiveQuestion(question,1);
        Assert.assertEquals(questionViewModel.getQuestionText().getValue(),question.getQuestion());
        List<String> alts = questionViewModel.getQuestionAlts().getValue();
        for(int i = 0; i < alts.size();i++){
            Assert.assertEquals(alts.get(i),question.getAlternatives().get(i));
        }
        Assert.assertEquals(questionViewModel.getQuestionTime().getValue().intValue(),question.getTime());
    }
}
