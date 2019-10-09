package com.god.kahit.ViewModel;

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

    private MutableLiveData<List<Pair<Player, Integer>>> listForView;

    public TeamArrangementViewModel() {
        BUS.register(this);
    }

    public MutableLiveData<List<Pair<Player, Integer>>> getListForView() {
        if (listForView == null) {
            listForView = new MutableLiveData<>();
            addNewPlayer();
        }
        return listForView;
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
        listForView.setValue(playerList);
    }

    public void addNewPlayer() {
        Repository.getInstance().addNewPlayer();
    }

    public void removePlayer(Player player) {
        Repository.getInstance().removePlayer(player);
    }

    public void resetPlayerData() {
        Repository.getInstance().resetPLayerData();
    }

    public void updatePlayerData(int position, int newTeamId) {
        Repository.getInstance().changeTeam(listForView.getValue().get(position).first, newTeamId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}

