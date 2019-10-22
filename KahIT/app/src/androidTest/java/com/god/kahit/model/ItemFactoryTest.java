package com.god.kahit.model;

import androidx.test.platform.app.InstrumentationRegistry;

import com.god.kahit.databaseService.ItemDataLoaderRealtime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ItemFactoryTest {

    @Before
    public void setup() {
        ItemFactory.setDataLoader(new ItemDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getTargetContext()));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateStoreItems() {
        List<Item> items = ItemFactory.createStoreItems(0);
        Assert.assertTrue(items.isEmpty());
        items = ItemFactory.createStoreItems(100);
        Assert.assertEquals(items.size(),300);
        List<Item> items2 = ItemFactory.createStoreItems(100);
        Assert.assertNotEquals(items,items2);

        items = ItemFactory.createStoreItems(-1);
        Assert.assertTrue(items.isEmpty());
    }
}
