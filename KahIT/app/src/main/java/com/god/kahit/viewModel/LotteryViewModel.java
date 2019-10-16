package com.god.kahit.viewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Item;
import com.god.kahit.model.Player;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LotteryViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = LotteryViewModel.class.getSimpleName();

    private MutableLiveData<Map<Player, Item>> mapWinningsLiveData;
    private MutableLiveData<List<Item>> itemListLiveData;
    private MutableLiveData<List<Player>> playerListLiveData;

    public LotteryViewModel() {
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
            loadMapWinningsLiveData();
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


    private void loadMapWinningsLiveData() {
        mapWinningsLiveData.setValue(Repository.getInstance().getDrawResult());
    }

    private void loadItemListLiveData() {
        itemListLiveData.setValue(Repository.getInstance().getAllItems());
    }

    private void loadPlayerListLiveData() {
        playerListLiveData.setValue(Repository.getInstance().getPlayers());
    }
}