package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemFactory {
    private static IItemDataLoader dataLoader;

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