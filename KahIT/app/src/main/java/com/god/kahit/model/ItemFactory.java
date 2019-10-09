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
        List<Item> loadedItems = dataLoader.getItems();
        ArrayList<Item> itemList = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            int index = r.nextInt(loadedItems.size());
            itemList.add(loadedItems.get(index));
        }
        return itemList;
    }

    /**
     * Sets the DataLoader for the factory
     * @param dataLoader the implementation of IItemDataLoader that the factory will use
     */

    public static void setDataLoader(IItemDataLoader dataLoader) {
        ItemFactory.dataLoader = dataLoader;
    }
}