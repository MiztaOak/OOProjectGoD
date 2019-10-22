package com.god.kahit.Events;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.Player;

public class PlayerVotedCategoryEvent implements IEvent {
    private Player player;
    private String categoryId;

    public PlayerVotedCategoryEvent(Player player, String categoryId) {
        this.player = player;
        this.categoryId = categoryId;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
