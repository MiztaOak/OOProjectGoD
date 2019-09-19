package com.god.kahit.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A factory class used to create questions
 */
public class QuestionFactory {

    /**
     * A method that creates Map of questions with category as the key and a list of questions as the values
     * @param categories A array of the categories that will be inside the map
     * @return The map with category as the key and a list of questions as the values
     */
    public static Map<Category, List<Question>> getQuestionMap(Category[] categories){
        Map<Category,List<Question>> questions = new HashMap<>();
        QuestionDataLoader dataLoader = new QuestionDataLoader();
        for(int i = 0; i < categories.length; i++){
            questions.put(categories[i],dataLoader.getQuestion(categories[i]));
        }

        return questions;
    }
}
