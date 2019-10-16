package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;
/**
 * @responsibility: This class is responsible for
 *
 * @used-by: This class is used in the following classes:
 * QuizGame, Store, IItemDataLoader, ItemDataLoaderRealTime, Lottery, LotteryViewModel, LotteryView
 * Modifier, Repository, VanityItem, StoreViewModel and in the database.
 *
 * @author: Anas Alkoutli
 */
public class Store { //todo implement a method to restock store?
    private List<Item> storeItems;
    private List<Item> boughtItems;

    Store() {
        this.storeItems = ItemFactory.createStoreItems(3);
        boughtItems = new ArrayList<>();
    }

    /**
     * A method that checks if a given player can buy a given item
     *
     * @param i the index of the item the player wishes to buy
     * @param player which player wants to buy an item
     * @return returns if the the player's score is enough to buy the item which is true or false
     */
    public boolean isItemBuyable(int i, Player player){
        return player.getScore()>= storeItems.get(i).getPrice();
    }

    public boolean isItemBought(int i){
        return boughtItems.contains(storeItems.get(i));
    }

    public void buy(int i, Player player) { //todo pass along a player parameter
        Item item  = storeItems.get(i);
        boughtItems.add(item);
        setItemToPlayer(item, player);
        player.setScore(player.getScore()-item.getPrice());

    } //todo It is store's responsibility to check if current user has enough points (money)

    private void setItemToPlayer(Item item, Player player){
        if (item instanceof Buff) {
            player.setBuff((Buff) item);
        }else if(item instanceof Debuff){
            player.setDebuff((Debuff) item);
        } else {
            player.setVanityItem((VanityItem) item);
        }
    }

    public List<Item> getStoreItems() {
        if(storeItems == null){
            this.storeItems = ItemFactory.createStoreItems(3);
        }
        return storeItems;
    }

    public List<Item> getBoughtItems() {
        return boughtItems;
    }
}
