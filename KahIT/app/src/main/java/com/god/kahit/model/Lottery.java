package com.god.kahit.model;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Lottery {

    final int delay = 500;

    //inal private Handler handler = new Handler();
    private List<BuyableItem> items = ItemFactory.createStoreItems(3);
    private List<ImageView> imagePlayerList = new ArrayList<>();
    private List<TextView> textPlayerList = new ArrayList<>();
    private List<ImageView> itemPlayerList = new ArrayList<>();
    private Random random = new Random();
    //Map<Player,BuyableItem> newList;

    //public Handler getHandler() {
    //    return handler;
   // }

    public Random getRandom() {
        return random;
    }

    public List<BuyableItem> getItems() {
        return items;
    }

    public int getDelay() {
        return delay;
    }

    public List<ImageView> getImagePlayerList() {
        return imagePlayerList;
    }

    public List<ImageView> getItemPlayerList() {
        return itemPlayerList;
    }

    public List<TextView> getTextPlayerList() {
        return textPlayerList;
    }

    public void delaySeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
