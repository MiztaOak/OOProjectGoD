package com.god.kahit.model;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Lottery {


    // final private Handler handler = new Handler();
    // private List<ImageView> imagePlayerList = new ArrayList<>();
    // private List<TextView> textPlayerList = new ArrayList<>();
    // private List<ImageView> itemPlayerList = new ArrayList<>();

    final int delay = 500;
    @SuppressLint("UseSparseArrays")
    Map<Integer, lotteryPlayerMap> map = new HashMap<>();
    private List<BuyableItem> buffDebuffItems = ItemFactory.createStoreItems(3);
    private Random random = new Random();


    public Random getRandom() {
        return random;
    }

    public List<BuyableItem> getBuffDebuffItems() {
        return buffDebuffItems;
    }

    public int getDelay() {
        return delay;
    }

    public Map<Integer, lotteryPlayerMap> getMap() {
        return map;
    }


   /* public void delaySeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    */
}



