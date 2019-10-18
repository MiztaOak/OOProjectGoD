package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;
/**
 * @responsibility: This class is responsible for
 *
 * used-by: This class is used in the following classes:
 * QuizGame, Store, IItemDataLoader, ItemDataLoaderRealTime, Lottery, LotteryViewModel, LotteryView
 * Modifier, Repository, VanityItem, StoreViewModel and in the database.
 *
 * @author: Anas Alkoutli
 */
public class Store { //todo implement a method to restock store?
    private List<Item> storeItems;
    private List<Item> boughtItems;

    Store() {
        this.storeItems = ItemFactory.createStoreItems(3);
        boughtItems = new ArrayList<>();
    }
    /**
     * A method that checks if a given player can buy a given item
     *
     * @param i the index of the item the player wishes to buy
     * @param player which player wants to buy an item
     * @return returns if the player's score is enough to buy the item and if the player
     * doesn't have an item
     */
//TODO uncomment this when done testing
    public boolean isItemBuyable(int i, Player player){
        if (storeItems.get(i) instanceof Buff){
            return (player.getScore()>= storeItems.get(i).getPrice() && player.getScoreMultiplier() == 1
                    && player.getAmountOfTime() == 0 && player.getAmountOfAlternatives() == 0);
        }
        return player.getScore()>= storeItems.get(i).getPrice();
    }
    /**
     * A method that checks if an item is bought so players cannot buy the same item.
     *
     * @param i: the index of an item.
     * @return : boolean that indicates if an item is bought an the list of bought items contains it.
     */
    public boolean isItemBought(int i){
        return boughtItems.contains(storeItems.get(i));
    }
    /**
     * A method that lets a player buy an item.
     * @param i: the index of an item the player wishes to buy.
     * @param player: the player wishing to buy in item.
     */
    public void buy(int i, Player player) {
        Item item  = storeItems.get(i);
        boughtItems.add(item);
        setItemToPlayer(i, player);
        player.setScore(player.getScore()-item.getPrice());
    }
    /**
     * A method that sets the item to the player who bought it.
     * @param i: the index of the item which will be set to the player.
     * @param player: which player the item will be set to.
     */
    private void setItemToPlayer(int i, Player player){
        Item item = storeItems.get(i);
        if (item instanceof Buff) {
            player.setBuff((Buff) item);
        }else if(item instanceof Debuff){
            player.setDebuff((Debuff) item);
        } else {
            player.setVanityItem((VanityItem) item);
        }
    }

    /**
     * A method that clears all the bought items so store gets restocked and players can buy again
     */
    private void restockStore(){
        if (boughtItems.size() == storeItems.size()){
            boughtItems.clear();
        }
    }
    /**
     * A method that returns the items in store that are available for the player to buy.
     * @return List of items.
     */
    public List<Item> getStoreItems() {
        restockStore();
        return storeItems;
    }
    /**
     * A method that returns the bought items in store.
     *
     *  @return List of items.
     */
    public List<Item> getBoughtItems() {
        return boughtItems;
    }
}
