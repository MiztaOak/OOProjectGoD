package com.god.kahit.model;

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
    private Player player; //todo remove local instance of player

    public Store() {
        this.storeItems = ItemFactory.createStoreItems(6);
        this.player= new Player("Anas", String.valueOf(500));
    }

    public void buy(Item item, Player player) { //todo pass along a player parameter
        if (item instanceof Buff) {
            player.setBuff((Buff) item);
        }else if(item instanceof Debuff){
            player.setDebuff((Debuff) item);
        } else {
            player.setModifier((Modifier) item);
            //player.setVanityItem((VanityItem) item);
        }
    } //todo It is store's responsibility to check if current user has enough points (money)

    public Player getPlayer() {
        return player;
    } //todo remove


    public void setPlayer(Player player) {
        this.player = player;
    }  //todo remove

    public List<Item> getStoreItems() {
        if(storeItems == null){
            this.storeItems = ItemFactory.createStoreItems(6);
        }
        return storeItems;
    }

    public void setStoreItems(List<Item> storeItems) {
        this.storeItems = storeItems;
    }
}
