package com.god.kahit.model.modelEvents;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.Question;

/** Event used to send a question to a listener outside of the model
 *
 * used by: QuestionViewModel, QuizGame
 *
 * @author Johan Ek
 */
public class QuestionEvent implements IEvent {
    private final Question question;
    private final int numOfRepeats;

    public QuestionEvent(Question question, int numOfRepeats) {
        this.question = question;
        this.numOfRepeats = numOfRepeats;
    }

    public Question getQuestion() {
        return question;
    }

    public int getNumOfRepeats() {
        return numOfRepeats;
    }
}
