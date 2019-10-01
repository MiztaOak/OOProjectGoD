package com.god.kahit.ViewModel;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.widget.ImageView;

import com.god.kahit.model.Item;
import com.god.kahit.model.Lottery;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LotteryViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = LotteryViewModel.class.getSimpleName();
    Lottery lottery;

    private Random rand;
    private MutableLiveData<Map<Integer, String>> playerMap;
    private MutableLiveData<Map<Integer, Item>> lotteryItemMap;

    public LotteryViewModel() {
        lottery = new Lottery();
    }

    public MutableLiveData<Map<Integer, String>> getPlayerMap() {
        if (playerMap == null) {
            playerMap = new MutableLiveData<>();
            loadPlayerMap();
        }
        return playerMap;
    }

    public Lottery getLottery(){
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
        int i;
        Map<Integer, Item> map = new HashMap<>();
        for (i = 0; i < lottery.getBuffDebuffItems().size(); i++) {
            map.put(i, lottery.getBuffDebuffItems().get(i));
        }
        lotteryItemMap.setValue(map);
    }

    private void loadPlayerMap() {
        int i;
        Map<Integer, String> map = new HashMap<>();
        for (i = 0; i < 8; i++) { //TODO Size of list with player names
            String player = "Player"; //TODO get player names as list.
            map.put(i, player + i);
        }
        playerMap.setValue(map);
    }

    public Random getRandom() {
        return rand = new Random();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }


    public Item getWonItem(List<Item> items){
        Item wonItem = items.get(items.size()-1);
        Collections.shuffle(items);
      return wonItem;
    }
}
