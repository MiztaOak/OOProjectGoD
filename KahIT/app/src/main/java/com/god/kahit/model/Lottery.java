package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class Lottery {
    private List<Item> items;
    private List<Player> players = new ArrayList<>(); //TODO remove when testing is done

    public Lottery() {
        this.items = ItemFactory.createStoreItems(3);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Item> getItems() {
        return items;
    }

    //TODO move methods from the viewModel and add listener
}



