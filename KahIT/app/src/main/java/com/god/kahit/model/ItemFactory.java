package com.god.kahit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * responsibility:  A class that is responsible for creating a list of items
 * which are used in Lottery and Store.
 * used-by: This class is used in the following classes:
 * Lottery, Store and Repository.
 *
 * @author Anas Alkoutli & Johan Ek
 */
public class ItemFactory {
    private static IItemDataLoader dataLoader;

    private ItemFactory() {
    }

    /**
     * A static method that creates a list of items
     *
     * @param size int that specifies how many items are needed in the list
     * @return list of items
     */
    static List<Item> createStoreItems(int size) {
        if (dataLoader == null) {
            return null;
        }
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.addAll(loadBuffs(size));
        itemList.addAll(loadDeBuffs(size));
        itemList.addAll(loadVanityItems(size));
        return itemList;
    }

    /**
     * Sets the DataLoader for the factory
     *
     * @param dataLoader the implementation of IItemDataLoader that the factory will use
     */
    public static void setDataLoader(IItemDataLoader dataLoader) {
        ItemFactory.dataLoader = dataLoader;
    }

    /**
     * Method that loads n buffs from the dataLoader
     *
     * @param n the amount of items that are loaded/returned
     * @return a list with n buffs
     */
    static private List<Buff> loadBuffs(int n) {
        List<Buff> loadedItems = dataLoader.getBuffs();
        List<Buff> createdItems = new ArrayList<>();
        Collections.shuffle(loadedItems);
        if (loadedItems.size() != 0) {
            for (int i = 0; i < n; i++) {
                createdItems.add(createBuff(loadedItems.get(i % loadedItems.size())));
            }
        }

        return createdItems;
    }

    /**
     * Method that copies a buff
     *
     * @param dataBuff the buff that will be copied
     * @return the copy of dataBuff
     */
    private static Buff createBuff(Buff dataBuff) {
        String name = dataBuff.getName();
        int price = dataBuff.getPrice();
        double scoreMulti = dataBuff.getScoreMultiplier();
        int amountOfAlts = dataBuff.getAmountOfAlternatives();
        int amountOfTime = dataBuff.getAmountOfTime();
        String id = dataBuff.getId();

        return new Buff(name, price, scoreMulti, amountOfTime, amountOfAlts, id);
    }

    /**
     * Method that loads n debuffs from the dataLoader
     *
     * @param n the amount of items that are loaded/returned
     * @return a list with n debuffs
     */
    static private List<Debuff> loadDeBuffs(int n) {
        List<Debuff> loadedItems = dataLoader.getDebuffs();
        Collections.shuffle(loadedItems);
        List<Debuff> createdItems = new ArrayList<>();
        Collections.shuffle(loadedItems);
        if (loadedItems.size() != 0) {
            for (int i = 0; i < n; i++) {
                createdItems.add(createDebuff(loadedItems.get(i % loadedItems.size())));
            }
        }
        return createdItems;
    }

    /**
     * Method that copies a debuff
     *
     * @param dataDebuff the debuff that will be copied
     * @return the copy of dataDebuff
     */
    private static Debuff createDebuff(Debuff dataDebuff) {
        String name = dataDebuff.getName();
        int price = dataDebuff.getPrice();
        double scoreMulti = dataDebuff.getScoreMultiplier();
        int amountOfTime = dataDebuff.getAmountOfTime();
        String id = dataDebuff.getId();
        boolean autoMode = dataDebuff.getAutoAnswer();

        return new Debuff(price, name, scoreMulti, amountOfTime, autoMode, id);
    }

    /**
     * Method that loads n vanity items from the dataLoader
     *
     * @param n the amount of items that are loaded/returned
     * @return a list with n vanity items
     */
    static private List<VanityItem> loadVanityItems(int n) {
        List<VanityItem> loadedItems = dataLoader.getVanityItems();
        Collections.shuffle(loadedItems);
        List<VanityItem> createdItems = new ArrayList<>();
        Collections.shuffle(loadedItems);
        if (loadedItems.size() != 0) {
            for (int i = 0; i < n; i++) {
                createdItems.add(createVanityItem(loadedItems.get(i % loadedItems.size())));
            }
        }
        return createdItems;
    }

    /**
     * Method that copies a vanity item
     *
     * @param dataVanityItem the vanity item that will be copied
     * @return a copy of dataVanityItem
     */
    private static VanityItem createVanityItem(VanityItem dataVanityItem) {
        String name = dataVanityItem.getName();
        int price = dataVanityItem.getPrice();
        String id = dataVanityItem.getId();

        return new VanityItem(price, name, id);
    }
}
