package com.god.kahit.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lottery {
    private List<Item> itemList;
    private Map<Player, Item> winnings;

    public Lottery() {
        this.itemList = ItemFactory.createStoreItems(3);
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public Map<Player, Item> drawItem(List<Player> playerList) {
        winnings = new HashMap<>();
        for(Player player: playerList) {
            Collections.shuffle(this.itemList);
            winnings.put(player, itemList.get(0));
        }
        return winnings;
    }
    //TODO is this needed?
    public Map<Player, Item> getWinnings() {
        return winnings;
    }
}



