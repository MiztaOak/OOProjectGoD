package com.god.kahit;

import androidx.test.platform.app.InstrumentationRegistry;

import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.model.Item;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class IItemDataLoaderRealtimeTest {
    private ItemDataLoaderRealtime dataLoaderRealtime;

    @Before
    public void setup(){
        dataLoaderRealtime = new ItemDataLoaderRealtime(InstrumentationRegistry.getInstrumentation().getContext());
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetItems(){
        List<Item> items = dataLoaderRealtime.getItems();
        Assert.assertFalse(items.isEmpty());
        for(Item item: items){
            Assert.assertNotNull(ItemDataLoaderRealtime.getItemImageNameMap().get(item.getName()));
        }
    }
}
