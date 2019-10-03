package com.god.kahit.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lottery {

    private Item item;
    private Map<Integer, Item> lotteryItemMap;
    private List<Item> buffDebuffItems;

    public Lottery() {
        this.lotteryItemMap = new HashMap<>();
        this.buffDebuffItems = ItemFactory.createStoreItems(3);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Map<Integer, Item> getLotteryItemMap() {
        return lotteryItemMap;
    }

    public void setLotteryItemMap(Map<Integer, Item> lotteryItemMap) {
        this.lotteryItemMap = lotteryItemMap;
    }

    public List<Item> getBuffDebuffItems() {
        return buffDebuffItems;
    }

    public void setBuffDebuffItems(List<Item> buffDebuffItems) {
        this.buffDebuffItems = buffDebuffItems;
    }
}



