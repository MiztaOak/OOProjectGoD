package com.god.kahit.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @responsibility: This class is responsible for the Lottery in the game.
 * <p>
 * used-by: This class is used in the following classes:
 * QuizGame.
 * @author: Oussama Anadani
 */
public class Lottery {
    private List<Item> itemList; // buffs or debuffs
    private Map<Player, Item> winnings; // won item that player gets

    Lottery() {
        this.itemList = ItemFactory.createStoreItems(3);
    }

    List<Item> getItemList() {
        return itemList;
    }

    /**
     * This method does the lottery.
     * It assigns the winning item to the players.
     *
     */
    Map<Player, Item> drawItem(List<Player> playerList) {
        winnings = new HashMap<>();
        for (Player player : playerList) {
            Collections.shuffle(this.itemList);
            winnings.put(player, itemList.get(0));
        }
        return winnings;
    }

    public Map<Player, Item> getWinnings() {
        return winnings;
    }
}



