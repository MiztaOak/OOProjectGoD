package com.god.kahit;

import com.god.kahit.model.Category;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuestionFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class QuestionTests {
    @Test
    public void questionDataLoaderTest(){
        Category[] categories = {Category.Nature};
        Map<Category, List<Question>> questionMap = QuestionFactory.getQuestionMap(categories);
        Question question = questionMap.get(Category.Science).get(0);
        Assert.assertEquals(question.getQuestion(),"Hur många ostar har Johan hemma?");
    }
}
