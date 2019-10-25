package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.Player;

/**
 * Event used to notify a observer that a certain player has voted on a category
 *
 * used by: CategoryView, CategoryViewModel, Repository
 *
 * @author Mats Cedervall
 */
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
