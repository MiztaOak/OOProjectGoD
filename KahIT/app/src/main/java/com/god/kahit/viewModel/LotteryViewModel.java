package com.god.kahit.ViewModel;

import android.util.Log;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Item;
import com.god.kahit.model.Player;
import com.god.kahit.model.modelEvents.LotteryDrawEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * responsibility: This class is responsible for the LiveData of lottery in the game.
 * used-by: LotteryView
 *
 * @author Mats Cedervall, Jakob Ewerstrand
 */
public class LotteryViewModel extends ViewModel implements LifecycleObserver {
    private static final String TAG = LotteryViewModel.class.getSimpleName();

    private MutableLiveData<Map<Player, Item>> mapWinningsLiveData;
    private MutableLiveData<List<Item>> itemListLiveData;
    private MutableLiveData<List<Player>> playerListLiveData;

    public LotteryViewModel() {
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d(TAG, "onStart");

        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
        Repository.getInstance().drawLottery();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        BUS.unregister(this);
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
        if (playerListLiveData == null) {
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

    /**
     * recieves a LotteryDrawEvent containing the winnings to be displayed.
     *
     * @param event LotteryDrawEvent containing a map with players and items.
     */
    @Subscribe
    public void onLotteryDrawEvent(LotteryDrawEvent event) {
        Log.d(TAG, "onLotteryDrawEvent: triggered");
        mapWinningsLiveData.setValue(event.getWinnings());
    }
}