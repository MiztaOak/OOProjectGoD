package com.god.kahit.model;

<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> storeBranch
import java.util.List;

public class Store {
    private List<BuyableItem> storeItems;
    public Store() {
<<<<<<< HEAD
       this.storeItems = ItemFactory.createStoreItems(3);
=======
       this.storeItems = ItemFactory.createStoreItems(1);
        System.out.println(storeItems.get(0).getName() + ".....................................-.-.-..-.-.-.....................................-.-.-..-.-.-.....................................-.-.-..-.-.-.....................................-.-.-..-.-.-.....................................-.-.-..-.-.-.");

>>>>>>> storeBranch
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
