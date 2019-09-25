package com.god.kahit.model;

import java.util.List;

public class Store {
    private List<BuyableItem> storeItems;

    public Store() {
       this.storeItems = ItemFactory.createStoreItems(3);
    }

    void buy(BuyableItem item, Player player){
        player.addItem(item);
        storeItems.remove(item);
    }


    public List<BuyableItem> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(List<BuyableItem> storeItems) {
        this.storeItems = storeItems;
    }
}
