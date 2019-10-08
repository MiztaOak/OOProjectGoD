package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class Lottery {
    private List<Item> items;
    private List<Player> players = new ArrayList<>();//todo

    public Lottery() {
        this.items = ItemFactory.createStoreItems(3);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Item> getItems() {
        return items;
    }

}



