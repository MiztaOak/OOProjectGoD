package com.god.kahit.model;

import com.god.kahit.ItemDataLoaderMock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LotteryTest {
    private Lottery lottery;


    @Before
    public void setup(){
        ItemFactory.setDataLoader(new ItemDataLoaderMock());
        lottery = new Lottery();
    }

    @Test
    public void drawItem() {
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            players.add(new Player("player "+i,"p"+i));
        }

        Map<Player,Item> drawnItems = lottery.drawItem(players);
        for(Player p: players){
            Assert.assertNotNull(drawnItems.get(p));
        }
    }
}