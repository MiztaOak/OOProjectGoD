package com.god.kahit.model;

import java.util.List;

public interface IQuestionDataLoader {
    List<Question> getQuestionList(Category category);
}
