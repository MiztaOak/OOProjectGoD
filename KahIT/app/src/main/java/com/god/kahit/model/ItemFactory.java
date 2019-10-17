package com.god.kahit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    static List<Item> createStoreItems(int size) {
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.addAll(loadBuffs(size));
        itemList.addAll(loadDeBuffs(size));
        itemList.addAll(loadVanityItems(size));
        return itemList;
    }

    /**
     * Sets the DataLoader for the factory
     * @param dataLoader the implementation of IItemDataLoader that the factory will use
    */
    public static void setDataLoader(IItemDataLoader dataLoader) {
        ItemFactory.dataLoader = dataLoader;
    }

    /**
     * Method that loads n buffs from the dataLoader
     * @param n the amount of items that are loaded/returned
     * @return a list with n buffs
     */
    static private List<Buff> loadBuffs(int n){
        List<Buff> loadedItems = dataLoader.getBuffs();
        Collections.shuffle(loadedItems);
        return loadedItems.subList(0,n);
    }

    /**
     * Method that loads n debuffs from the dataLoader
     * @param n the amount of items that are loaded/returned
     * @return a list with n debuffs
     */
    static private List<Debuff> loadDeBuffs(int n){
        List<Debuff> loadedItems = dataLoader.getDebuffs();
        Collections.shuffle(loadedItems);
        return loadedItems.subList(0,n);
    }

    /**
     * Method that loads n vanity items from the dataLoader
     * @param n the amount of items that are loaded/returned
     * @return a list with n vanity items
     */
    static private List<VanityItem> loadVanityItems(int n){
        List<VanityItem> loadedItems = dataLoader.getVanityItems();
        Collections.shuffle(loadedItems);
        return loadedItems.subList(0,n);
    }
}
