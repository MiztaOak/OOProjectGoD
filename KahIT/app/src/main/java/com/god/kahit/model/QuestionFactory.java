package com.god.kahit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A factory class used to create questions
 *
 * Used by: Repository, QuizGame
 *
 * @author Johan Ek
 */
public class QuestionFactory {
    /**
     * Variable that hold the implementation of the dataLoader
     */
    private static IQuestionDataLoader dataLoader;

    private QuestionFactory(){}

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
        for (Category category : categories) {
            questions.put(category, getQuestionList(category));
        }

        return questions;
    }

    /**
     * Method that copies the questions from the dataLoader to avoid any strange alias errors
     *
     * @param category The category that the questions should have
     * @return a list of questions that all share the same category and is a copy of the list from
     * the dataLoader
     */
    private static List<Question> getQuestionList(Category category) {
        List<Question> loadedQuestions = dataLoader.getQuestionList(category);
        List<Question> createdQuestions = new ArrayList<>();

        for (Question q : loadedQuestions) {
            createdQuestions.add(copyQuestion(q));
        }

        return createdQuestions;
    }

    private static Question copyQuestion(Question q){
        return new Question(q.getCategory(), q.getQuestionText(), q.getAnswer(), new ArrayList<>(q.getAlternatives()), q.getTime());
    }

    /**
     * Method that gets a map containing all possible categories
     *
     * @return The map with category as the key and a list of questions as the values
     */
    static Map<Category, List<Question>> getFullQuestionMap() {
        List<Category> categories = Category.getRealCategories();
        return getQuestionMap((Category[]) categories.toArray());
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
