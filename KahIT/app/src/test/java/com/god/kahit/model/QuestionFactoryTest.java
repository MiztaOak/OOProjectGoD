package com.god.kahit.model;

import com.god.kahit.QuestionDataLoaderMock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class QuestionFactoryTest {
    @Before
    public void setup(){
        QuestionFactory.setDataLoader(new QuestionDataLoaderMock());
    }

    @Test
    public void getQuestionMapTest(){
        Category[] categories = {Category.History,Category.Nature,Category.Religion};
        Map<Category, List<Question>> questionMap = QuestionFactory.getQuestionMap(categories);


    }

    private void testQuestionMap(Category[] categories, Map<Category,List<Question>> questionMap){
        for(Category category: categories){
            List<Question> questions = questionMap.get(category);
            Assert.assertNotNull(questions);
            for(Question question: questions){
                Assert.assertNotNull(question);
                Assert.assertNotNull(question.getQuestionText());
                Assert.assertNotNull(question.getAnswer());
                Assert.assertNotNull(question.getCategory());
                Assert.assertNotNull(question.getAlternatives());
                for(String alt: question.getAlternatives()){
                    Assert.assertNotNull(alt);
                }
            }
        }
    }

    @Test
    public void getFullQuesionMapTest(){
        List<Category> categories = Category.getRealCategories();
        Map<Category,List<Question>> questionMap = QuestionFactory.getFullQuestionMap();
        testQuestionMap(categories.toArray(new Category[]{}),questionMap);
    }

    @Test
    public void nullDataLoaderTest(){
        QuestionFactory.setDataLoader(null);
        Assert.assertNull(QuestionFactory.getFullQuestionMap());
        Category[] categories = {Category.Religion,Category.Nature};
        Assert.assertNull(QuestionFactory.getQuestionMap(categories));
    }
}
