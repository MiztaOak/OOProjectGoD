package com.god.kahit.model;

/**
 * @responsibility: This class is responsible for all items which can be bought by players.
 * This class holds all the common values between different items like buffs,
 * debuffs and vanity items
 * @used-by: This class is used in the following classes:
 * QuizGame, Store, IItemDataLoader, ItemDataLoaderRealTime, Lottery, LotteryViewModel, LotteryView
 * Modifier, Repository, VanityItem, StoreViewModel and in the database.
 * @author: Anas Alkoutli
 */
public abstract class Item {
    private int price;
    private String name;

    public Item(int price, String name) {
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

}
