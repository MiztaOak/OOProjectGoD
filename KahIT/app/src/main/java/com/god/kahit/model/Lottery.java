package com.god.kahit.model;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Lottery {


    private Map<Integer, lotteryItem> map = new HashMap<>();
    private List<BuyableItem> buffDebuffItems = ItemFactory.createStoreItems(3);

    public List<BuyableItem> getBuffDebuffItems() {
        return buffDebuffItems;
    }
    public Map<Integer, lotteryItem> getMap() {
        return map;
    }


}



