package com.god.kahit.model;

import java.util.List;

public class Store {
    private List<Item> storeItems;

    public Store() {
        this.storeItems = ItemFactory.createStoreItems(4);
    }

    public void buy(Item item) {
        storeItems.remove(item);
    }

    public List<Item> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(List<Item> storeItems) {
        this.storeItems = storeItems;
    }
}
