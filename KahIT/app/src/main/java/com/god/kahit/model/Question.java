package com.god.kahit.model;

public class Question {
    private final Category category;
    private final String question;
    private final String answer;
    private final String[] options;

    public Question(Category category,String question, String answer, String[] options){
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.options = options;
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

    public String[] getOptions() {
        return options;
    }
}
