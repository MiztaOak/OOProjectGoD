package com.god.kahit;

import android.support.test.InstrumentationRegistry;

import com.god.kahit.databaseService.QuestionDataLoaderDB;
import com.god.kahit.model.Category;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuestionFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * A test class testing QuestionFactory and its helper class QuestionDataLoader
 */
public class QuestionFactoryTest {
    @Test
    public void testQuestionDataLoader(){
        Map<Category, List<Question>> qMap;
        Category[] categories = {Category.Test};
        Assert.assertEquals(QuestionFactory.getQuestionMap(categories),null);
        QuestionFactory.setDataLoader(new QuestionDataLoaderDB(InstrumentationRegistry.getInstrumentation().getTargetContext()));
        qMap = QuestionFactory.getQuestionMap(categories);
        for(int i = 0; i < categories.length; i++){
            List<Question> list = qMap.get(categories[i]);
            Assert.assertNotEquals(list.size(),0);
            if(categories[i] == Category.Test){
                Assert.assertEquals(list.size(),5);
                Assert.assertEquals(list.get(0).getQuestion(),"Hur många ostar har Johan hemma?");
                Assert.assertEquals(list.get(1).getQuestion(),"Test");
                Assert.assertEquals(list.get(1).getAnswer(),"Test1");
                Boolean[] tests = {false,false,false,false};
                for (String alt:list.get(1).getAlternatives()) {
                    if(alt.equals("Test1")){
                        tests[0] = true;
                    }else if(alt.equals("Test2")){
                        tests[1] = true;
                    }else if(alt.equals("Test3")){
                        tests[2] = true;
                    }else if(alt.equals("Test4")){
                        tests[3] = true;
                    }

                }
                for (Boolean t:tests) {
                    Assert.assertTrue(t);
                }
            }
        }

    }

    @Test
    public void testQuestionFactory(){
        Map<Category, List<Question>> qMap;
        Category[] categories = {Category.History, Category.Science, Category.Nature, Category.Test};
        QuestionFactory.setDataLoader(new QuestionDataLoaderDB(InstrumentationRegistry.getInstrumentation().getTargetContext()));
        qMap = QuestionFactory.getQuestionMap(categories);
        Assert.assertTrue(qMap.containsKey(Category.History)); //These test assert that all asked for categories are present and that they are not empty
        Assert.assertTrue(qMap.containsKey(Category.Nature));
        Assert.assertTrue(qMap.containsKey(Category.Test));
        Assert.assertTrue(qMap.containsKey(Category.Science));
        Assert.assertTrue(qMap.get(Category.Science).size() != 0);
        Assert.assertTrue(qMap.get(Category.History).size() != 0);
        Assert.assertTrue(qMap.get(Category.Nature).size() != 0);
        Assert.assertTrue(qMap.get(Category.Test).size() != 0);
    }
}
