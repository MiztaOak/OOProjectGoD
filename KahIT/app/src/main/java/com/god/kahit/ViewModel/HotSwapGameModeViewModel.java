package com.god.kahit.ViewModel;

import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HotSwapGameModeViewModel extends ViewModel implements LifecycleObserver {

    private MutableLiveData<List<String>> gameModes;

    public HotSwapGameModeViewModel() {
    }

    public MutableLiveData<List<String>> getGameModes() {
        if (gameModes == null) {
            gameModes = new MutableLiveData<>();
        }
        return gameModes;
    }

}
