package com.god.kahit.model;

public interface QuizListener { //todo Use bus instead?, rename to question listener
    void receiveQuestion(Question q, int n);
}
