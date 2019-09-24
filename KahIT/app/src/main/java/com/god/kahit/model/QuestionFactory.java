package com.god.kahit.model;

import android.content.Context;

import com.god.kahit.databaseService.QuestionDataLoaderDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A factory class used to create questions
 */
public class QuestionFactory {
    /**
     * Variable that hold the implementation of the dataLoader
     */
    private static IQuestionDataLoader dataLoader;

    /**
     * A method that creates Map of questions with category as the key and a list of questions as the values
     * @param categories A array of the categories that will be inside the map
     * @return The map with category as the key and a list of questions as the values
     */
    public static Map<Category, List<Question>> getQuestionMap(Category[] categories){
        if(dataLoader == null)
            return null;
        Map<Category,List<Question>> questions = new HashMap<>();
        for(int i = 0; i < categories.length; i++){
            questions.put(categories[i],dataLoader.getQuestionList(categories[i]));
        }

        return questions;
    }

    static Map<Category, List<Question>> getFullQuestionMap(){
        Category[] categories = {Category.Science, Category.History, Category.Nature};
        return getQuestionMap(categories);
    }

    public static void setDataLoader(IQuestionDataLoader dataLoader) {
        QuestionFactory.dataLoader = dataLoader;
    }
}
