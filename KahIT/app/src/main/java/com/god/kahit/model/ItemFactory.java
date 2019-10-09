package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemFactory {
    private static IItemDataLoader dataLoader;

   static String[][] items = {{"150", "Buff", "Double Score", "double_score", "2", "0", "0"}
            , {"120", "Buff", "Time Headstart", "time_headstart", "1", "5", "0"}
            , {"100", "Buff", "Fifty Fifty", "fifty_fifty", "1", "0", "2"}
           , {"100", "Debuff", "Half Score", "half_score", "1", "0", "0"}
           , {"80", "Debuff", "Time is money", "double_time", "1", "-5", "0"}
           , {"80", "Debuff", "Let the robot do it", "binoculars", "1", "0", "0"}}; //TODO replace with database

    static public List<Item> createStoreItems(int size) {
        List<Item> loadedItems = dataLoader.getItems()
        ArrayList<Item> itemList = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            int index = r.nextInt(loadedItems.size());
            itemList.add(loadedItems.get(index));
        }
        return itemList;
    }

    public static String[][] getItems() {
        return items;
    }

    public static String getCertainItem(int row, int col) {
        return items[row][col];
    }

    public static void setDataLoader(IItemDataLoader dataLoader) {
        ItemFactory.dataLoader = dataLoader;
    }
}