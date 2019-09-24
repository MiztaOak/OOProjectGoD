package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {
    static String[][] items = {{"50", "Buff", "Double Score", "double_score", "2", "1", "0", "0"}
    , {"50", "Buff", "Double Time", "double_time", "1", "2", "0", "0"}
    , {"50", "Buff", "Time Headstart", "time_headstart", "1", "1", "10", "0"}
    , {"50", "Buff", "  ", "time_headstart", "1", "1", "10", "0"}};

    static public List<BuyableItem> createStoreItems(int size){

        ArrayList<BuyableItem> itemList = new ArrayList<>();
        for (int i= 0; i < size; i++){
            String[] item = items[i];
            itemList.add(new StatAlterer(Integer.parseInt(item[0]), item[1], item[2],item[3], Integer.parseInt(item[4]),Integer.parseInt(item[5]), Integer.parseInt(item[6]), Integer.parseInt(item[7])));
        }

        return itemList;
    }
}
