package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class Lottery {
    private List<Item> buffDebuffItems;
    private List<Player> players = new ArrayList<>(); //TODO remove when testing is done

    public Lottery() {

    }

    public List<Player> getPlayers() {
        return players;
    }

    //todo later, 8 players
    public void initPlayers(int numOfPlayers) {
        for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
            players.add(new Player());
        }
    }

    public List<Item> getBuffDebuffItems() {
        if(buffDebuffItems == null){
            this.buffDebuffItems = ItemFactory.createStoreItems(3);
        }
        return buffDebuffItems;
    }

    //TODO move methods from the viewModel and add listener
}



