package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {

    static String[][] items = {{"150", "Buff", "Double Score", "double_score", "2", "0", "0"}
            , {"120", "Buff", "Time Headstart", "time_headstart", "1", "5", "0"}
            , {"100", "Buff", "Fifty Fifty", "fifty_fifty", "1", "0", "2"}
           , {"100", "Debuff", "Half Score", "half_score", "1", "0", "0"}
           , {"80", "Debuff", "Time is money", "double_time", "1", "-5", "0"}
           , {"80", "Debuff", "Let the robot do it", "binoculars", "1", "0", "0"}}; //TODO replace with database

    static public List<Item> createStoreItems(int size) {

        ArrayList<Item> itemList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int rand = (int) (Math.random() * (items.length));
            String[] item = items[rand];
            itemList.add(new Modifier(Integer.parseInt(item[0]), item[1], item[2], item[3], Integer.parseInt(item[4]), Integer.parseInt(item[5]), Integer.parseInt(item[6])));
        }
        return itemList;
    }

    public static String[][] getItems() {
        return items;
    }

    public static String getCertainItem(int row, int col) {
        return items[row][col];
    }
}