package com.god.kahit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionFactory {

    public static Map<Category, List<Question>> getQuestionMap(Category[] categories){
        Map<Category,List<Question>> questions = new HashMap<>();

        for(int i = 0; i < categories.length; i++){
            questions.put(categories[i],getQuestionList(categories[i]));
        }

        return questions;
    }

    private static List<Question> getQuestionList(Category category){
        List<Question> questionList = new ArrayList<>();
        QuestionDataLoader dataLoader = new QuestionDataLoader("testQuestionDataBase.txt");

        questionList.add(dataLoader.getQuestion(category));
        return questionList;
    }
}
