package com.god.kahit.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QuestionTest {

    @Test
    public void scrambleAlternatives() {
        List<String> stringList = new ArrayList<>();
        Category category = Category.Science;
        stringList.add("HELLO");
        stringList.add("WORLD");
        stringList.add("SCRAMBLE");
        stringList.add("JOKE");
        Question question = new Question(category, "2", "3", stringList,10);
        List<String> scram = question.scrambleAlternatives(stringList);
        assertNotEquals(scram, stringList);
    }
}