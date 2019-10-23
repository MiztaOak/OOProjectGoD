package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.Player;

public class PlayerAnsweredQuestionEvent implements IEvent {
    private Player player;
    private String givenAnswer;

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
