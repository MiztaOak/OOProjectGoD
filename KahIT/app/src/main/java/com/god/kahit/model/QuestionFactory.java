package com.god.kahit.model;

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

        for(int i = 0; i < categories.length; i++){
            questions.put(categories[i],getQuestionList(categories[i]));
        }

        return questions;
    }

    /**
     * Method that creates a list of question based on a given category
     * @param category the category that the list will focus on
     * @return the list of questions
     */
    private static List<Question> getQuestionList(Category category){
        List<Question> questionList = new ArrayList<>();
        QuestionDataLoader dataLoader = new QuestionDataLoader("testQuestionDataBase.txt");

        questionList.add(dataLoader.getQuestion(category));
        return questionList;
    }
}
