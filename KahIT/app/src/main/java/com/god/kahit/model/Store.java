package com.god.kahit.model;

import java.util.List;

public class Store {
    private List<BuyableItem> storeItems;

    void buy(BuyableItem item, User user){

    }


    public List<BuyableItem> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(List<BuyableItem> storeItems) {
        this.storeItems = storeItems;
    }
}
