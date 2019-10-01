package com.god.kahit.controller;

import com.god.kahit.R;
import com.god.kahit.view.QuestionClass;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.doNothing;

public class QuestionClassTest{

    @InjectMocks
    private QuestionClass questionClass;

    @Mock
    private QuestionClass questionClassMock;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void launchScorePageClass() {
    }

    @Test
    public void greyOutAnswersTextView() {
    }

    @Test
    public void testOnAnswerClicked() {

    }

    @Test
    public void testPopulateQuestionTextView() throws Exception {
        //QuestionClass  mockWorld = mock(QuestionClass.class);
        final String expected = "HELLO";


        doNothing().when(questionClassMock).populateQuestionTextView(anyString());

    }

    @Test
    public void populateAnswerTextViews() {
    }

    @Test
    public void populatePlayerName() {
    }

    @Test
    public void populateQuestionNum() {
    }

    @Test
    public void populateTotalNumQuestions() {
    }
}