package com.god.kahit.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class QuestionTest {

    @Test
    public void scrambleAlternatives() {
        List<String> stringList = new ArrayList<>();
        Category category = Category.Science;
        stringList.add("HELLO");
        stringList.add("WORLD");
        stringList.add("SCRAMBLE");
        stringList.add("JOKE");
        Question question = new Question(category, "2", "3", stringList, 10);
        List<String> scram;
        do {
            scram = question.scrambleAlternatives(stringList);
        } while (!scram.equals(stringList));
        assertTrue(true);
    }

    @Test
    public void isCorrectAnswer() {
        Category category = Category.Gaming;
        String questionText = "Test";

        String wrongAnswer = "WrongAnswer";
        String correctAnswer = "CorrectAnswer";

        Question question = new Question(category, questionText, correctAnswer, new ArrayList<String>(), 1);

        Assert.assertFalse(question.isCorrectAnswer(wrongAnswer));
        Assert.assertTrue(question.isCorrectAnswer(correctAnswer));
    }
}