package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {

        ArrayList<BuyableItem> predefinedList = new ArrayList<>();

    static public List<BuyableItem> createStoreItems(int size){

        ArrayList<BuyableItem> itemList = new ArrayList<BuyableItem>();
        for (int i=0 ;size > i; size--){
            itemList.add(new StatAlterer(50, "Buff", "double score", "source",  2, 1, 0, 0));
        }

        return itemList;
    }
}
