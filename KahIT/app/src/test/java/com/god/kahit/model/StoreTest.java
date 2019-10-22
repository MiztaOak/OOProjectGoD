package com.god.kahit.model;

import com.god.kahit.ItemDataLoaderMock;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreTest {
    private Store store;
    Player player;

    @Before
    public void setup(){
        ItemFactory.setDataLoader(new ItemDataLoaderMock());
        store = new Store();
        player = new Player("player","p");
    }

    @Test
    public void isItemBuyable() {
        player.setScore(0);
        assertFalse(store.isItemBuyable(0,player));
        player.setScore(10000);
        assertTrue(store.isItemBuyable(0,player));
    }

    @Test
    public void isItemBought() {
        player.setScore(300);
        assertFalse(store.isItemBought(0));
        store.buyItem(0,player);
        assertTrue(store.isItemBought(0));
    }

    @Test
    public void buy() {
        player.setScore(300);
        Item item = store.getStoreItems().get(0);

        store.buyItem(0,player);

        assertEquals(player.getScore(), 300 - item.getPrice());

    }
}