package com.god.kahit.ViewModel;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;


import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.model.Lobby;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.god.kahit.model.Lobby.BUS;

public class HotSwapAddPlayersViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = HotSwapAddPlayersViewModel.class.getSimpleName();

    private Lobby lobby;

    private MutableLiveData<List<String>> playerMap;

    public HotSwapAddPlayersViewModel() {
        lobby = new Lobby(); //TODO
        BUS.register(this);
    }

    public MutableLiveData<List<String>> getPlayerMap() {
        if (playerMap == null) {
            playerMap = new MutableLiveData<>();
            addOnePlayer();
        }
        return playerMap;
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        List<String> playerNames = new ArrayList<>();
        int numOfTeams = event.getTeam().size();
        for (int i = 0; i < numOfTeams; i++) {
            for (int j = 0; j < event.getTeam().get(i).getTeamMembers().size(); j++) {
                String playerName = event.getTeam().get(i).getTeamMembers().get(j).getName();
                playerNames.add(playerName);
            }
        }
        playerMap.setValue(playerNames);
    }

    public void addOnePlayer() {
        lobby.addPlayerToTeam(lobby.createNewPlayer(), lobby.getTeamList().size());
    }

    public void removeOnePlayer() {
        lobby.deleteTeam(lobby.getTeamList().size()); //TODO remove one player not a team.
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}
