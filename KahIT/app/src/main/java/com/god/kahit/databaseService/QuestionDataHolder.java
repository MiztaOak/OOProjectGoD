package com.god.kahit.databaseService;

import com.god.kahit.model.Category;
import com.god.kahit.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionDataHolder {
    private List<String> alts;
    private String answer;
    private String question;
    private Long time;

    public  QuestionDataHolder(){}

    public QuestionDataHolder(List<String> alts, String answer, String question, Long time) {
        this.alts = alts;
        this.answer = answer;
        this.question = question;
        this.time = time;
    }

    public Question createQuestion(Category category){
        List<String> newAlts = new ArrayList<>(alts);
        newAlts.add(answer);
        return new Question(category,question,answer,newAlts,time.intValue());
    }

    public List<String> getAlts() {
        return alts;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public Long getTime() {
        return time;
    }
}
