package com.god.kahit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A class that represents a question. The class holds all data that a question needs and methods
 * for scrambling the alternatives and telling if the answer that was given is correct
 *
 * used by: IQuestionDataLoader, QuestionDataLoaderRealtime, QuestionFactory, QuestionViewModel,
 * QuizGame, QuestionEvent, Repository
 *
 * @author Johan Ek & Jakob Ewerstrand
 */
public class Question {
    private final Category category;
    private final String question;
    private final String answer;
    private final List<String> alternatives;
    private int time;

    public Question(Category category, String question, String answer, List<String> alternatives, int time) {
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.alternatives = scrambleAlternatives(alternatives);
        this.time = time;
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

    /**
     * Scrambles alternatives so that they don't have to appear on the same place in the list.
     *
     * @param alt is a list Strings with of alternatives.
     * @return altCopy is a scrambled copy of alt.
     */
    List<String> scrambleAlternatives(List<String> alt) {
        List<String> altCopy = new ArrayList<>(alt);
        Collections.shuffle(altCopy);
        return altCopy;
    }

    public boolean isCorrectAnswer(String givenAnswer) {
        return givenAnswer.equals(answer);
    }

}