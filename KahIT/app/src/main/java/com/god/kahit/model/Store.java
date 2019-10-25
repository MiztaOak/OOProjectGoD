package com.god.kahit.model;

import com.god.kahit.model.modelEvents.DebuffPlayerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * responsibility: This class is responsible for the store in the game. It handles the
 * information need to buyItem items for different players.
 * <p>
 * used-by: This class is used in the following classes:
 * QuizGame.
 *
 * @author: Anas Alkoutli & Johan Ek
 */
public class Store {
    private final List<Item> storeItems;
    private final List<Item> boughtItems;
    private final IEventBus bus;

    Store(IEventBus bus) {
        this.storeItems = ItemFactory.createStoreItems(3);
        boughtItems = new ArrayList<>();

        this.bus = bus;
    }

    /**
     * A method that checks if a given player can buyItem a given item
     *
     * @param i      the index of the item the player wishes to buyItem
     * @param player which player wants to buyItem an item
     * @return returns if the player's score is enough to buyItem the item and if the player
     * doesn't have an item
     */
    boolean isItemBuyable(int i, Player player) {
        if (storeItems.get(i) instanceof Buff) {
            return (player.getScore() >= storeItems.get(i).getPrice() && player.getScoreMultiplier() == 1
                    && player.getAmountOfTime() == 0 && player.getAmountOfAlternatives() == 0);
        }
        return player.getScore() >= storeItems.get(i).getPrice();
    }

    /**
     * A method that checks if an item is bought so players cannot buyItem the same item.
     *
     * @param i: the index of an item.
     * @return : boolean that indicates if an item is bought an the list of bought items contains it.
     */
    boolean isItemBought(int i) {
        return boughtItems.contains(storeItems.get(i));
    }

    /**
     * A method that lets a player buyItem an item.
     *
     * @param i:      the index of an item the player wishes to buyItem.
     * @param player: the player wishing to buyItem in item.
     */
    void buyItem(int i, Player player) {
        Item item = storeItems.get(i);
        boughtItems.add(item);
        setItemToPlayer(i, player);
        player.setScore(player.getScore() - item.getPrice());
    }

    /**
     * A method that sets the item to the player who bought it.
     *
     * @param i:      the index of the item which will be set to the player.
     * @param player: which player the item will be set to.
     */
    void setItemToPlayer(int i, Player player) {
        Item item = storeItems.get(i);
        if (item instanceof Buff) {
            player.setBuff((Buff) item);
        } else if (item instanceof Debuff) {
            bus.post(new DebuffPlayerEvent((Debuff) item));
        } else {
            player.setVanityItem((VanityItem) item);
        }
    }

    /**
     * A method that clears all the bought items so store gets re-stocked and players can buyItem again.
     */
    private void restockStore() {
        if (boughtItems.size() == storeItems.size()) {
            boughtItems.clear();
        }
    }

    /**
     * A method that returns the items in store that are available for the player to buyItem.
     *
     * @return List of items.
     */
    List<Item> getStoreItems() {
        restockStore();
        return storeItems;
    }

    /**
     * A method that returns the bought items in store.
     *
     * @return List of items.
     */
    List<Item> getBoughtItems() {
        return boughtItems;
    }
}
