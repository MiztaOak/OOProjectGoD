package com.god.kahit.viewModel;

import androidx.lifecycle.ViewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Item;

import java.util.List;

/**
 * @responsibility: A ViewModel class that handles the information needed for the view.
 * It handles the connection to the Repository which has connection to its  model Store.
 * @used-by: This class is used in the following classes:
 * StoreView.
 * @author: Anas Alkoutli
 */
public class StoreViewModel extends ViewModel {
    /**
     * A method that returns if the player is able to buy an item.
     *
     * @param i: the index of the item the player wishes to buy.
     * @return : boolean which indicates if a player is able to buy the item.
     */
    public boolean isItemBuyable(int i) {
        return Repository.getInstance().isItemBuyable(i);
    }

    /**
     * A method that let's the player buy an item.
     *
     * @param i: the index of the item the player wishes to buy.
     */
    public void buy(int i) {
        Repository.getInstance().buy(i);
    }

    /**
     * @return
     */
    public List<Item> getStoreItems() {
        return Repository.getInstance().getStoreItems();
    }

    /**
     * A method that returns the name of an item.
     *
     * @param i: the index of the item.
     * @return : A string with the name of the item.
     */
    public String getItemName(int i) {
        return Repository.getInstance().getItemName(i);
    }

    /**
     * A method that returns the price of an item.
     *
     * @param i: the index of the item.
     * @return : int with the price of the item.
     */
    public int getItemPrice(int i) {
        return Repository.getInstance().getItemPrice(i);
    }

    /**
     * A method that returns the players score.
     *
     * @return : int value of player's score.
     */
    public int getPlayerPoints() {
        return Repository.getInstance().getPlayerScore();
    }

    /**
     * A method that returns if an item is bought or available to buy
     *
     * @param i: The index of the item.
     * @return : boolean which indicates if the item is bought or not.
     */
    public boolean isItemBought(int i) {
        return Repository.getInstance().isItemBought(i);
    }
}
