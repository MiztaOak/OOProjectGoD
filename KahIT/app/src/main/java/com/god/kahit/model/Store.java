package com.god.kahit.model;

import java.util.List;

public class Store {
    private List<Item> storeItems;
    private Player player;

    public Store() {
        this.player= new Player("Anas", 500);
    }


    public void buy(Item item) {
        player.setWonItem(item);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
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
