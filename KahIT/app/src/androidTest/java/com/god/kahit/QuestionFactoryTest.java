package com.god.kahit;

import com.god.kahit.databaseService.QuestionDataLoaderRealtime;
import com.god.kahit.model.Category;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuestionFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

/**
 * A test class testing QuestionFactory and its helper class QuestionDataLoader
 */
@RunWith(AndroidJUnit4.class)
public class QuestionFactoryTest {

    @Before
    public void setUp() throws InterruptedException {
        QuestionFactory.setDataLoader(new QuestionDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getTargetContext()));
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testQuestionDataLoader() {
        Map<Category, List<Question>> qMap;
        Category[] categories = {Category.Test};
        qMap = QuestionFactory.getQuestionMap(categories);
        List<Question> list = qMap.get(Category.Test);
        Assert.assertNotEquals(list.size(), 0);
        Assert.assertEquals(list.size(), 5);
        Assert.assertEquals(list.get(0).getQuestion(), "Hur många ostar har Johan hemma?");
        Assert.assertEquals(list.get(1).getQuestion(), "Test");
        Assert.assertEquals(list.get(1).getAnswer(), "Test1");
        Boolean[] tests = {false, false, false, false};
        for (String alt : list.get(1).getAlternatives()) {
            if (alt.equals("Test1")) {
                tests[0] = true;
            } else if (alt.equals("Test2")) {
                tests[1] = true;
            } else if (alt.equals("Test3")) {
                tests[2] = true;
            } else if (alt.equals("Test4")) {
                tests[3] = true;
            }
        }
        for (Boolean t : tests) {
            Assert.assertTrue(t);
        }
    }

    @Test
    public void testQuestionFactory() {
        Map<Category, List<Question>> qMap;
        Category[] categories = {Category.History, Category.Science, Category.Nature, Category.Test};
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
