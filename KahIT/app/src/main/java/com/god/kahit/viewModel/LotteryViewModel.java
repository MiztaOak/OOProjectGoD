package com.god.kahit.ViewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Item;
import com.god.kahit.model.Player;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @responsibility: This class is responsible for the LiveDAta of lottery in the game.
 * @used-by: LotteryView
 * @author: Jakob Ewerstrand
 */
public class LotteryViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = LotteryViewModel.class.getSimpleName();

    private MutableLiveData<Map<Player, Item>> mapWinningsLiveData;
    private MutableLiveData<List<Item>> itemListLiveData;
    private MutableLiveData<List<Player>> playerListLiveData;

    public LotteryViewModel() {
    }

    public void drawLottery() {
        Repository.getInstance().drawLottery();
    }

    public MutableLiveData<List<Item>> getItemListLiveData() {
        if (itemListLiveData == null) {
            itemListLiveData = new MutableLiveData<>();
            loadItemListLiveData();
        }
        return itemListLiveData;
    }

    public MutableLiveData<Map<Player, Item>> getMapWinningsLiveData() {
        if (mapWinningsLiveData == null) {
            mapWinningsLiveData = new MutableLiveData<>();
        }
        return mapWinningsLiveData;
    }

    public MutableLiveData<List<Player>> getPlayerListLiveData() {
        if(playerListLiveData == null) {
            playerListLiveData = new MutableLiveData<>();
            loadPlayerListLiveData();
        }
        return playerListLiveData;
    }

    private void loadItemListLiveData() {
        itemListLiveData.setValue(Repository.getInstance().getAllItems());
    }

    private void loadPlayerListLiveData() {
        playerListLiveData.setValue(Repository.getInstance().getPlayers());
    }
}