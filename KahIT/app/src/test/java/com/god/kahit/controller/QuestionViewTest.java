package com.god.kahit.controller;

import com.god.kahit.view.QuestionView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;

public class QuestionViewTest {

    @InjectMocks
    private QuestionView questionView;

    @Mock
    private QuestionView questionViewMock;

    @Before
    public void init() {
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
        //QuestionView  mockWorld = mock(QuestionView.class);
        final String expected = "HELLO";
        //doNothing().when(questionViewMock).populateQuestionTextView(anyString());
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