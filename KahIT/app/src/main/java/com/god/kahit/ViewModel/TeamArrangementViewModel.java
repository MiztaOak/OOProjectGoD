package com.god.kahit.ViewModel;

import android.util.Log;

import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.Repository;
import com.god.kahit.model.Player;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class TeamArrangementViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = TeamArrangementViewModel.class.getSimpleName();

    private MutableLiveData<List<Player>> playerList;

    public TeamArrangementViewModel() {
        BUS.register(this);
    }

    public MutableLiveData<List<Player>> getPlayerList() {
        if (playerList == null) {
            playerList = new MutableLiveData<>();
            addNewPlayer();
        }
        return playerList;
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
    }

    public void addNewPlayer() {
        Repository.getInstance().addNewPlayer();
    }

    public void removeOnePlayer() {
        //Repository.getInstance().removePlayer();
    }


    public void resetPlayerData() {
        Repository.getInstance().resetPLayerData();
    }




    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}

