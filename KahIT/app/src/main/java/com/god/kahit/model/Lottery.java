package com.god.kahit.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @responsibility: This class is responsible for the Lottery in the game.
 * @used-by: QuizGame.
 * @author: Oussama Anadani
 */
public class Lottery {
    private List<Item> itemList;
    private Map<Player, Item> winnings;

    Lottery() {
        this.itemList = ItemFactory.createStoreItems(3); // number of buffs and debuffs in the item factory
    }

    List<Item> getItemList() {
        return itemList;
    }

    /**
     * This method does the lottery.
     * It assigns the winning item to the players.
     *
     * @param playerList A list of players that the winning item will be assigned to.
     */
    Map<Player, Item> drawItem(List<Player> playerList) {
        winnings = new HashMap<>();
        for (Player player : playerList) {
            Collections.shuffle(this.itemList);
            winnings.put(player, itemList.get(0));
        }
        return winnings;
    }
}


