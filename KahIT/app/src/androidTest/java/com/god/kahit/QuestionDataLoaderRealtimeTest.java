package com.god.kahit;

import android.os.Looper;

import com.god.kahit.databaseService.QuestionDataLoaderRealtime;
import com.god.kahit.model.Category;
import com.god.kahit.model.Question;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.test.platform.app.InstrumentationRegistry;

public class QuestionDataLoaderRealtimeTest {
    private QuestionDataLoaderRealtime dataLoaderRealtime;
    @Before
    public void setup(){

        try {
            dataLoaderRealtime = new QuestionDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getTargetContext());
        }catch (Exception e){
            Looper.prepare();
            dataLoaderRealtime = new QuestionDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getTargetContext());
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetQuestionList(){
        Category category = Category.History;
        List<Question> questions = dataLoaderRealtime.getQuestionList(category);
        Assert.assertTrue(questions.size() > 0);
        Assert.assertEquals(questions.get(0).getQuestion(),"Which culture invented the plow?");
    }
}
