package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that creates a list of items which are later used inte Lottery and Store
 */

public class ItemFactory {
    private static IItemDataLoader dataLoader;

    /**
     * A static method that creates a list of items
     * @param size int that specifies how many items are needed in the list
     * @return list of items
     */

    static public List<Item> createStoreItems(int size) {
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.addAll(createBuffs());
        itemList.addAll(createDeBuffs());
        return itemList;
    }

    /**
     * Sets the DataLoader for the factory
     * @param dataLoader the implementation of IItemDataLoader that the factory will use
     */

    public static void setDataLoader(IItemDataLoader dataLoader) {
        ItemFactory.dataLoader = dataLoader;
    }

    static private List<Item> createBuffs(){
        List<Item> loadedItems = dataLoader.getItems();
        ArrayList<Item> buffsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            buffsList.add(loadedItems.get(i));
        }
        return buffsList;
    }

    static private List<Item> createDeBuffs(){
        List<Item> loadedItems = dataLoader.getItems();
        ArrayList<Item> buffsList = new ArrayList<>();
        for (int i = 3; i < 6; i++) {
            buffsList.add(loadedItems.get(i));
        }
        return buffsList;
    }

    static private List<Item> createVanityItems(){
        List<Item> loadedItems = dataLoader.getItems();
        ArrayList<Item> buffsList = new ArrayList<>();
        for (int i = 6; i < 9; i++) {
            buffsList.add(loadedItems.get(i));
        }
        return buffsList;
    }
}
