package com.god.kahit.viewModel;

import android.util.Log;

import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.Repository;
import com.god.kahit.model.Player;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class HotSwapAddPlayersViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = HotSwapAddPlayersViewModel.class.getSimpleName();

    private MutableLiveData<List<Player>> playerListForView;
    private MutableLiveData<List<Integer>> teamNumberForView;

    public HotSwapAddPlayersViewModel() {
    }

    public MutableLiveData<List<Player>> getPlayerListForView() {
        if (playerListForView == null) {
            playerListForView = new MutableLiveData<>();
        }
        return playerListForView;
    }

    public MutableLiveData<List<Integer>> getTeamNumberForView() {
        if (teamNumberForView == null) {
            teamNumberForView = new MutableLiveData<>();
            Repository.getInstance().fireTeamChangeEvent();
        }
        return teamNumberForView;
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        List<Player> sortedPlayerList = new ArrayList<>();
        List<Integer> teamNumberList = new ArrayList<>();

        //Already existing items added first in order.
        if (playerListForView.getValue() != null)
            for (Player player : playerListForView.getValue()) {
                for (int i = 0; i < event.getTeams().size(); i++) {
                    if (event.getTeams().get(i).getTeamMembers().contains(player)) {
                        sortedPlayerList.add(player);
                        teamNumberList.add(i);
                    }
                }
            }

        //New additions to the list added.
        for (int i = 0; i < event.getTeams().size(); i++) {
            for (int j = 0; j < event.getTeams().get(i).getTeamMembers().size(); j++) {
                if (!sortedPlayerList.contains(event.getTeams().get(i).getTeamMembers().get(j))) {
                    sortedPlayerList.add(event.getTeams().get(i).getTeamMembers().get(j));
                    teamNumberList.add(i);
                }
            }
        }
        playerListForView.setValue(sortedPlayerList);
        teamNumberForView.setValue(teamNumberList);
    }

    public void addNewPlayer() {
        Repository.getInstance().addNewPlayer();
    }

    public void removePlayer(int position) {
        Repository.getInstance().removePlayer(Objects.requireNonNull(playerListForView.getValue()).get(position));
    }

    public void resetPlayerData() {
        Repository.getInstance().resetPLayerData();
    }

    public void updatePlayerData(int position, int newTeamId) {
        Repository.getInstance().changeTeam(Objects.requireNonNull(playerListForView.getValue()).get(position), newTeamId);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        BUS.unregister(this);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}
