package com.god.kahit.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lottery {

    private BuyableItem item;
    private Map<Integer, BuyableItem> lotteryItemMap;
    private List<BuyableItem> buffDebuffItems;

    public Lottery() {
        this.lotteryItemMap = new HashMap<>();
        this.buffDebuffItems = ItemFactory.createStoreItems(3);
    }

    public BuyableItem getItem() {
        return item;
    }

    public void setItem(BuyableItem item) {
        this.item = item;
    }

    public Map<Integer, BuyableItem> getLotteryItemMap() {
        return lotteryItemMap;
    }

    public void setLotteryItemMap(Map<Integer, BuyableItem> lotteryItemMap) {
        this.lotteryItemMap = lotteryItemMap;
    }

    public List<BuyableItem> getBuffDebuffItems() {
        return buffDebuffItems;
    }

    public void setBuffDebuffItems(List<BuyableItem> buffDebuffItems) {
        this.buffDebuffItems = buffDebuffItems;
    }
}



