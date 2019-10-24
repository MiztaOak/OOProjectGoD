package com.god.kahit;

import com.god.kahit.model.Category;
import com.god.kahit.model.IQuestionDataLoader;
import com.god.kahit.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionDataLoaderMock implements IQuestionDataLoader {
    @Override
    public List<Question> getQuestionList(Category category) {
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String[] alts = {"answer" + i, "alt1", "alt2", "alt3"};
            questions.add(new Question(category, "Question text " + i, "answer " + i, new ArrayList<>(Arrays.asList(alts)), 10));
        }

        return questions;
    }
}
