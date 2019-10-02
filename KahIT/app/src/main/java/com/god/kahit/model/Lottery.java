package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class Lottery {


    private List<Item> buffDebuffItems;
    private List<Player> players = new ArrayList<>();


    public Lottery() {
        this.buffDebuffItems = ItemFactory.createStoreItems(3);


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
        return buffDebuffItems;
    }

}



