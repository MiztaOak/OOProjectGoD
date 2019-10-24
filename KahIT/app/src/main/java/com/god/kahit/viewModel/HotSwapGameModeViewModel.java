package com.god.kahit.viewModel;

import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.GameMode;

/**
 * responsibility: The viewModel for the HotSwapGameModeView.
 * No real responsibility now since implementation is not completed.
 *
 * used-by: HotSwapGameModeView.
 *
 * @author Jakob Ewerstrand
 */
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

    public void setUpNewGameInstance() {
        Repository.getInstance().setupNewGameInstance(GameMode.HOT_SWAP);
    }

}
