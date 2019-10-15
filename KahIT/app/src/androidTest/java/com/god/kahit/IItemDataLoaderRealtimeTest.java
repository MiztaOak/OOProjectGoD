package com.god.kahit;

import androidx.test.platform.app.InstrumentationRegistry;

import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.model.Buff;
import com.god.kahit.model.Debuff;
import com.god.kahit.model.Item;
import com.god.kahit.model.VanityItem;

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
        List<Buff> buffs = dataLoaderRealtime.getBuffs();
        List<Debuff> debuffs = dataLoaderRealtime.getDebuffs();
        List<VanityItem> vanityItems = dataLoaderRealtime.getVanityItems();
        Assert.assertFalse(buffs.isEmpty());
        Assert.assertFalse(debuffs.isEmpty());
        Assert.assertFalse(vanityItems.isEmpty());
        for(Item item: buffs){
            Assert.assertNotNull(ItemDataLoaderRealtime.getItemImageNameMap().get(item.getName()));
        }

        for(Item item: debuffs){
            Assert.assertNotNull(ItemDataLoaderRealtime.getItemImageNameMap().get(item.getName()));
        }

        for(Item item: vanityItems){
            Assert.assertNotNull(ItemDataLoaderRealtime.getItemImageNameMap().get(item.getName()));
        }
    }
}
