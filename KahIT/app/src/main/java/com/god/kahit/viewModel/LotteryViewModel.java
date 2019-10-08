package com.god.kahit.ViewModel;

import com.god.kahit.model.Item;
import com.god.kahit.model.Lottery;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LotteryViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = LotteryViewModel.class.getSimpleName();
    Lottery lottery;
    private Random rand = new Random();
    private MutableLiveData<Map<Integer, String>> playerMap;
    private MutableLiveData<Map<Integer, Item>> lotteryItemMap;

   /* public LotteryViewModel() {
        lottery = new Lottery();
        lottery.initPlayers(8);
    }*/

    public MutableLiveData<Map<Integer, String>> getPlayerMap() {
        if (playerMap == null) {
            playerMap = new MutableLiveData<>();
            loadPlayerMap();
        }
        return playerMap;
    }

    private void loadPlayerMap() {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 8; i++) { //TODO Size of list with player names
            String player = "Player"; //TODO get player names as list.
            map.put(i, player + i);
        }
        playerMap.setValue(map);
    }

    public Lottery getLottery() {
        return lottery;
    }

    public MutableLiveData<Map<Integer, Item>> getLotteryItemMap() {
        if (lotteryItemMap == null) {
            lotteryItemMap = new MutableLiveData<>();
            loadItemMap();
        }
        return lotteryItemMap;
    }

    private void loadItemMap() {
        Map<Integer, Item> map = new HashMap<>();
        for (int i = 0; i < lottery.getItems().size(); i++) {
            map.put(i, lottery.getItems().get(i));


            for (i = 0; i < lottery.getItems().size(); i++) {
                map.put(i, lottery.getItems().get(i));

            }
            lotteryItemMap.setValue(map);
        }
    }

    public Item getWonItem(List<Item> items) {
        Item wonItem = items.get(items.size() - 1);
        Collections.shuffle(items);
        return wonItem;
    }

    public Random getRandom() {
        return rand = new Random();
    }

    public void setWonItem(Item wonItem, int playerIndex) {
        lottery.getPlayers().get(playerIndex).setWonItem(wonItem);

    }
}