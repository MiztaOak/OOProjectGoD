package com.god.kahit.viewModel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.GameMode;

import java.util.List;

/**
 * responsibility: The viewModel for the HotSwapGameModeView.
 * No real responsibility now since implementation is not completed.
 * <p>
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

    /**
     * Sets up a new HotSwap game by calling repository.
     */
    public void setUpNewGameInstance() {
        Repository.getInstance().setupNewGameInstance(GameMode.HOT_SWAP);
    }

}
