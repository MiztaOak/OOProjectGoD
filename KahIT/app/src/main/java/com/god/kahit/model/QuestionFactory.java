package com.god.kahit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionFactory {

    public static Map<Category, List<Question>> getQuestionMap(){
        Map<Category,List<Question>> questions = new HashMap<>();


        return questions;
    }

    private static List<Question> getQuestionList(Category category){
        List<Question> questionList = new ArrayList<>();

        return questionList;
    }
}
