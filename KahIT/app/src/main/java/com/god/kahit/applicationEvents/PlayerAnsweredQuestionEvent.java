package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.Player;

/**
 * Event used to notify a observer that a certain player has answered a questions and what the answer
 * was.
 *
 * used by: QuestionView, Repository
 *
 * @author Mats Cedervall
 */
public class PlayerAnsweredQuestionEvent implements IEvent {
    private final Player player;
    private final String givenAnswer;

    public PlayerAnsweredQuestionEvent(Player player, String givenAnswer) {
        this.player = player;
        this.givenAnswer = givenAnswer;
    }

    public Player getPlayer() {
        return player;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }
}
