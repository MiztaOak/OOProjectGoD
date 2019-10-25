package com.god.kahit.databaseService;

import com.god.kahit.model.Item;

/**
 * Abstract Class that holds the data that is common across all items, the data is is fetched from
 * the database, it also has a abstract method that will be used to creates a Item
 * based on it's data.
 *
 * used by: ItemDataLoaderRealtime, ModifierDataHolder, VanityItemDataHolder
 *
 * @author Johan Ek
 */
abstract class ItemDataHolder {
    private int price;
    private String type;
    private String name;
    private String img_name;
    private String id;

    ItemDataHolder() {
    }

    int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getImg_name() {
        return img_name;
    }

    String getId() {
        return id;
    }

    public abstract Item createItem();
}
