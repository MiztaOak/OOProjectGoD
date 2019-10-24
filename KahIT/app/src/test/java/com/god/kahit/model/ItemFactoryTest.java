package com.god.kahit.model;

import org.junit.Assert;
import org.junit.Test;

public class ItemFactoryTest {
    @Test
    public void nullDataLoaderTest(){
        ItemFactory.setDataLoader(null);
        Assert.assertNull(ItemFactory.createStoreItems(3));
    }
}
