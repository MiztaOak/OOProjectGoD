package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private List<BuyableItem> storeItems;
    public Store() {
       this.storeItems = ItemFactory.createStoreItems(1);
        System.out.println(storeItems.get(0).getName() + ".....................................-.-.-..-.-.-.....................................-.-.-..-.-.-.....................................-.-.-..-.-.-.....................................-.-.-..-.-.-.....................................-.-.-..-.-.-.");

    }

    void buy(BuyableItem item, User user){
        storeItems.remove(item);
    }


    public List<BuyableItem> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(List<BuyableItem> storeItems) {
        this.storeItems = storeItems;
    }
}
