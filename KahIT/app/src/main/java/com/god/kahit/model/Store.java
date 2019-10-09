package com.god.kahit.model;

import java.util.List;

public class Store { //todo implement a method to restock store?
    private List<Item> storeItems;
    private Player player; //todo remove local instance of player

    public Store() {
        this.storeItems = ItemFactory.createStoreItems(6);
        this.player= new Player("Anas", String.valueOf(500));
    }

    public void buy(Item item) { //todo pass along a player parameter
        player.setHeldItem((Modifier) item);
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
