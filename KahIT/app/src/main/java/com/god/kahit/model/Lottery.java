package com.god.kahit.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Lottery view that takes care of showing players' name up and the randomized item(Buff or Debuff) that they get *
 * the concept of "items" means which Buff or Debuff that the player gets *
 * Debuffs are the items that player can send to other players and negatively affect them *
 * Buffs are the items that players positively can affect themselves *
 */
public class Lottery {
    private List<Item> itemList;  // buffs or debuffs
    private Map<Player, Item> winnings; // the won item that that player gets in the lottery

    public Lottery() {
        this.itemList = ItemFactory.createStoreItems(3); //TODO replace with more "dynamic" way to set this
    }

    List<Item> getItemList() {
        return itemList;
    }

    /**
     * assigns the won item to the player
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



