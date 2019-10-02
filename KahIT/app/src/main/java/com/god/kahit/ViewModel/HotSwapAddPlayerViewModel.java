package com.god.kahit.viewModel;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public class HotSwapAddPlayerViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = HotSwapAddPlayerViewModel.class.getSimpleName();

    private MutableLiveData<Map<Integer, String>> playerMap;

    public HotSwapAddPlayerViewModel() {
        getPlayerMap();
    }

    public MutableLiveData<Map<Integer, String>> getPlayerMap() {
        if (playerMap == null) {
            playerMap = new MutableLiveData<>();
            loadPlayerMap();
        }
        return playerMap;
    }

    private void loadPlayerMap() {
        int id = 1;
        String p1 = "Player 1";
        Map<Integer, String> map = new HashMap<>();
        map.put(id, p1);
        playerMap.setValue(map);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}
