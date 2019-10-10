package com.god.kahit.viewModel;

import android.util.Log;

import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.Repository;
import com.god.kahit.model.Player;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.core.util.Pair;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class TeamArrangementViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = TeamArrangementViewModel.class.getSimpleName();
    private Repository repository;
    private MutableLiveData<List<Pair<Player, Integer>>> playerListForView;

    public TeamArrangementViewModel() {
        repository = Repository.getInstance();
        BUS.register(this);
    }

    public MutableLiveData<List<Pair<Player, Integer>>> getPlayerListForView() {
        if (playerListForView == null) {
            playerListForView = new MutableLiveData<>();
            addNewPlayer();
        }
        return playerListForView;
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        List<Pair<Player, Integer>> playerList = new ArrayList<>();
        Pair<Player, Integer> playerIntegerPair;
        for (int i = 0; i < event.getTeams().size(); i++) {
            for (int j = 0; j < event.getTeams().get(i).getTeamMembers().size(); j++) {
                playerIntegerPair = new Pair<>(event.getTeams().get(i).getTeamMembers().get(j), i);
                playerList.add(playerIntegerPair);
            }
        }
        playerListForView.setValue(playerList);
    }

    public void addNewPlayer() {
        repository.addNewPlayer();
    }

    public void removePlayer(Player player) {
        repository.removePlayer(player);
    }

    public void resetPlayerData() {
        repository.resetPlayerData();
    }

    public void updatePlayerData(int position, int newTeamId) { //todo cant ever do this, can only request and wait for eventbus from viewModel
        repository.updatePlayerData(playerListForView.getValue().get(position).first, newTeamId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}

