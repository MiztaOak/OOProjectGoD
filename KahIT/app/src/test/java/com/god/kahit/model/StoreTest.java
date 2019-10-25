package com.god.kahit.model;

import com.god.kahit.EventBussMock;
import com.god.kahit.ItemDataLoaderMock;
import com.god.kahit.applicationEvents.EventBusGreenRobot;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StoreTest {
    private Store store;
    private Player player;


    @Before
    public void setup() {
        ItemFactory.setDataLoader(new ItemDataLoaderMock());
        store = new Store(new EventBussMock());
        player = new Player("player","p");
    }

    @Test
    public void isItemBuyable() {
        player.setScore(0);
        assertFalse(store.isItemBuyable(0, player));
        assertFalse(store.isItemBuyable(8, player));
        player.setScore(10000);
        assertTrue(store.isItemBuyable(0, player));
        assertTrue(store.isItemBuyable(8, player));
    }

    @Test
    public void isItemBought() {
        player.setScore(300);
        assertFalse(store.isItemBought(0));
        store.buyItem(0, player);
        assertTrue(store.isItemBought(0));
    }

    @Test
    public void buy() {
        player.setScore(300);
        Item item = store.getStoreItems().get(0);
        store.buyItem(0, player);
        assertEquals(player.getScore(), 300 - item.getPrice());

    }

    @Test
    public void setItemToPlayer() {
        store.setItemToPlayer(1, player);
        assertTrue(player.getAmountOfAlternatives() != 0 || player.getScoreMultiplier() != 1
                || player.getAmountOfTime() != 0 || player.isAutoAnswer());
    }

    @Test
    public void restockStore() {
        player.setScore(5000);
        for (int i = 0; i < store.getStoreItems().size(); i++) {
            store.buyItem(i, player);
        }
        assertEquals(0, store.getBoughtItems().size());
    }
}