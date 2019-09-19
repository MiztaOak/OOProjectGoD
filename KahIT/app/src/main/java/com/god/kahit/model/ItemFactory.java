package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {


    public List<BuyableItem> createStoreItems(int size){
        ArrayList<BuyableItem> itemList = new ArrayList<BuyableItem>();

        for (int i=0 ;size > i; size--){
            itemList.add(new BuyableItem());
        }

        return itemList;
    }
}
