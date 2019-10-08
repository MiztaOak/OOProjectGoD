package com.god.kahit.model;

import java.util.Collections;
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
     *
     * @param categories A array of the categories that will be inside the map
     * @return The map with category as the key and a list of questions as the values
     */
    public static Map<Category, List<Question>> getQuestionMap(Category[] categories) {
        if (dataLoader == null)
            return null;
        Map<Category, List<Question>> questions = new HashMap<>();
        for (int i = 0; i < categories.length; i++) {
            questions.put(categories[i], dataLoader.getQuestionList(categories[i]));
        }

        return questions;
    }

    /**
     * Method that gets a map containing all possible categories
     *
     * @return The map with category as the key and a list of questions as the values
     */
    static Map<Category, List<Question>> getFullQuestionMap() {
        List<Category> categories = Category.getRealCategories();
        return getQuestionMap((Category[])categories.toArray());
    }

    /**
     * Method that sets which implementation of the questionDataLoader is used by the factory. Basically
     * a implementation of dependency injection.
     *
     * @param dataLoader the implementation of questionDataLoader that is used by the factory
     *                   determines how the question data is loader and from where.
     */
    public static void setDataLoader(IQuestionDataLoader dataLoader) {
        QuestionFactory.dataLoader = dataLoader;
    }
}
