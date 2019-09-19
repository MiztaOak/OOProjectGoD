package com.god.kahit.model;

import java.util.List;

public class Question {
    private final Category category;
    private final String question;
    private final String answer;
    private final List<String> alternatives;
    private int time;
    private boolean hasBeenAnswered;

    public Question(Category category,String question, String answer, List<String> alternatives,int time){
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.alternatives = alternatives;
        this.time = time;
        this.hasBeenAnswered = false;
    }

    public Category getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public int getTime() {
        return time;
    }

    public boolean isHasBeenAnswered() {
        return hasBeenAnswered;
    }
}
