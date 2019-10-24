package com.god.kahit.viewModel;

import androidx.lifecycle.ViewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Item;

/**
 * @responsibility: A ViewModel class that handles the information needed for the view.
 * It handles the connection to the Repository which has connection to its  model Store.
 * @used-by: This class is used in the following classes:
 * StoreView.
 * @author: Anas Alkoutli
 */
public class StoreViewModel extends ViewModel {
    /**
     * A method that returns if the player is able to buyItem an item.
     *
     * @param i: the index of the item the player wishes to buyItem.
     * @return : boolean which indicates if a player is able to buyItem the item.
     */
    public boolean isItemBuyable(int i) {
        return Repository.getInstance().isItemBuyable(i);
    }

    /**
     * A method that let's the player buyItem an item.
     *
     * @param i: the index of the item the player wishes to buyItem.
     */
    public void buy(int i) {
        Repository.getInstance().buy(i);
    }

    public Item getItem(int itemIndex) {
        return Repository.getInstance().getItem(itemIndex);
    }

    /**
     * A method that returns the players score.
     *
     * @return : int value of player's score.
     */
    public int getPlayerPoints() {
        return Repository.getInstance().getMyPlayerScore();
    }

    /**
     * A method that returns if an item is bought or available to buyItem
     *
     * @param i: The index of the item.
     * @return : boolean which indicates if the item is bought or not.
     */
    public boolean isItemBought(int i) {
        return Repository.getInstance().isItemBought(i);
    }
}
