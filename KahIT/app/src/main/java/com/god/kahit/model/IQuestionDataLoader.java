package com.god.kahit.model;

import java.util.List;

/**
 * Interface used to abstract the implementation of the persistent storage of questions
 * <p>
 * used by: QuestionDataLoaderRealtime, QuestionFactory
 *
 * @author Johan Ek
 */
public interface IQuestionDataLoader {
    List<Question> getQuestionList(Category category);
}
