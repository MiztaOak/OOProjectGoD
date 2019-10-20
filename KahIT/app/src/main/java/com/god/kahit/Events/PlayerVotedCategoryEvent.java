package com.god.kahit.Events;

import com.god.kahit.model.Category;
import com.god.kahit.model.Player;

public class PlayerVotedCategoryEvent {
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
