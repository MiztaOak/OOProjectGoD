package com.god.kahit.model;

import java.util.List;

public class Lottery {


    private List<Item> buffDebuffItems;
    private List<Player> players;

    public Lottery() {
        this.buffDebuffItems = ItemFactory.createStoreItems(3);
    }


    public List<Player> getPlayers() {
        return players;
    }


    //todo 8 players
    public void setPlayersName(int numOfPlayers) {
        for (int i = 0; i < numOfPlayers; i++) {
            players.get(i).setName("yo" + i);
        }
    }



    public List<Item> getBuffDebuffItems() {
        return buffDebuffItems;
    }

    public void setBuffDebuffItems(List<Item> buffDebuffItems) {
        this.buffDebuffItems = buffDebuffItems;
    }
}



