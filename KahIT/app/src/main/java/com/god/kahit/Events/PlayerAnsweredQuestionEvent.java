package com.god.kahit.Events;

import com.god.kahit.model.Player;

public class PlayerAnsweredQuestionEvent {
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
