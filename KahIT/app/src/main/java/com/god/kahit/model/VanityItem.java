package com.god.kahit.model;

/**
 * responsibility: This class is responsible for the cosmetic items in the game.
 * used-by: This class is used in the following classes:
 * Player, QuizGame, ItemFactory, Store, ItemDataLoaderRealtime and IItemDataLoader.
 *
 * @author Anas Alkoutli
 */
public class VanityItem extends Item {

    public VanityItem(int price, String name, String id) {
        super(price, name, id);
    }
}
