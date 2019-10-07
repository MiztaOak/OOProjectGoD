package com.god.kahit.ViewModel;

import android.util.Log;

import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.Repository;
import com.god.kahit.model.Player;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class HotSwapAddPlayersViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = HotSwapAddPlayersViewModel.class.getSimpleName();


    private MutableLiveData<List<Player>> playerMap;

    public HotSwapAddPlayersViewModel() {
        BUS.register(this);
    }

    public MutableLiveData<List<Player>> getPlayerMap() {
        if (playerMap == null) {
            playerMap = new MutableLiveData<>();
            addNewPlayer();
        }
        return playerMap;
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        /*List<Player> eventCopy = new ArrayList<>();
        for (int i =0; i <event.getTeams().size();i++) {

        }
        event.getTeams().contains()
        for(int i=0; i<event.getTeams().size(); i++) {
            for(int j=0; j<event.getTeams().get(i).getTeamMembers().size(); j++) {
                if(event.getTeams().get(i).getTeamMembers().get(j).equals(playerMap.getValue().get()))
            }
        }*/


        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < event.getTeams().size(); i++) {
            playerList.addAll(event.getTeams().get(i).getTeamMembers());
        }
        playerMap.setValue(playerList);
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
        Repository.getInstance().updatePlayerData(playerMap.getValue().get(position), newTeamId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}
