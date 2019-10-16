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
    private List<Item> boughtItems;

    private Player player; //todo remove local instance of player

    public Store() {
        this.storeItems = ItemFactory.createStoreItems(3);
        this.player= new Player("Anas", String.valueOf(500));
    }
    public boolean isItemBuyable(int i, Player player){
        return(player.getScore() >= storeItems.get(i).getPrice());
    }
    public void buy(int i, Player player) {
        Item item = storeItems.get(i);
        boughtItems.add(item);
        player.setScore(player.getScore() - item.getPrice());
        setItemToPlayer(item, player);
    }

    private void setItemToPlayer(Item item, Player player){
        if (item instanceof Buff) {
            player.setBuff((Buff) item);
        }else if(item instanceof Debuff){
            player.setDebuff((Debuff) item);
        } else {
            player.setVanityItem((VanityItem) item);
        }
    }
    public Player getPlayer() {
        return player;
    } //todo remove


    public void setPlayer(Player player) {
        this.player = player;
    }  //todo remove

    public List<Item> getStoreItems() {
        if(storeItems == null){
            this.storeItems = ItemFactory.createStoreItems(3);
        }
        return storeItems;
    }

    public void setStoreItems(List<Item> storeItems) {
        this.storeItems = storeItems;
    }
    public List<Item> getBoughtItems() {
        return boughtItems;
    }

    public void setBoughtItems(List<Item> boughtItems) {
        this.boughtItems = boughtItems;
    }
}
