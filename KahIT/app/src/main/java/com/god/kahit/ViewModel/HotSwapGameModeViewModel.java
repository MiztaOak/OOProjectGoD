package com.god.kahit.ViewModel;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

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
