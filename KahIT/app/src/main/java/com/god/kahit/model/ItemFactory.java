package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {

   static String[][] items = {{"50", "Buff", "Double Score", "double_score", "2", "1", "0", "0"}
            , {"60", "Buff", "Double Time", "double_time", "1", "2", "0", "0"}
            , {"70", "Buff", "Time Headstart", "time_headstart", "1", "1", "10", "0"}
            , {"80", "Buff", "Biceps", "strong", "1", "1", "10", "0"}};

    static public List<Item> createStoreItems(int size) {

        ArrayList<Item> itemList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int rand = (int) (Math.random()*(items.length));
            String[] item = items[rand];
            itemList.add(new Modifier(Integer.parseInt(item[0]), item[1], item[2], item[3], Integer.parseInt(item[4]), Integer.parseInt(item[5]), Integer.parseInt(item[6]), Integer.parseInt(item[7])));
        }
            return itemList;
    }


    public static String[][] getItems(){
        return items;
    }

    public static String getCertainItem(int row, int col){
        return items[row][col];
    }
}