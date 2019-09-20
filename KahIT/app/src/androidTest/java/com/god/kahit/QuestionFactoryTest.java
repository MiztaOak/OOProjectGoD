package com.god.kahit;

import android.support.test.InstrumentationRegistry;

import com.god.kahit.model.Category;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuestionFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class QuestionFactoryTest {
    @Test
    public void testQuestionDataLoader(){
        Map<Category, List<Question>> qMap;
        Category[] categories = {Category.test};
        qMap = QuestionFactory.getQuestionMap(categories, InstrumentationRegistry.getInstrumentation().getTargetContext());
        for(int i = 0; i < categories.length; i++){
            List<Question> list = qMap.get(categories[i]);
            Assert.assertNotEquals(list.size(),0);
            if(categories[i] == Category.test){
                Assert.assertEquals(list.size(),4);
                Assert.assertEquals(list.get(0).getQuestion(),"Hur många ostar har Johan hemma?");
                Assert.assertEquals(list.get(1).getQuestion(),"Test");
                Assert.assertEquals(list.get(1).getAnswer(),"Test1");
                Assert.assertEquals(list.get(1).getAlternatives().get(0),"Test1");
                Assert.assertEquals(list.get(1).getAlternatives().get(1),"Test2");
                Assert.assertEquals(list.get(1).getAlternatives().get(2),"Test3");
                Assert.assertEquals(list.get(1).getAlternatives().get(3),"Test4");
            }
        }
    }
}
