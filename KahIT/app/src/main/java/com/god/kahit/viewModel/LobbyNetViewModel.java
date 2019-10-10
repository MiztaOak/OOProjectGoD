package com.god.kahit.viewModel;

import android.util.Log;

import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.Repository;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.networkManager.Connection;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Objects;

import androidx.core.util.Pair;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class LobbyNetViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = LobbyNetViewModel.class.getSimpleName();
    private Repository repository;
    private MutableLiveData<List<Pair<Player, Connection>>> playerListForView;
    private MutableLiveData<List<Team>> teamListForView;
    private boolean isHost;

    public LobbyNetViewModel() {
        repository = Repository.getInstance();
        BUS.register(this);
    }

    public MutableLiveData<List<Pair<Player, Connection>>> getPlayerListForView() {
        if (playerListForView == null) {
            playerListForView = new MutableLiveData<>();
//            addNewPlayer();
        }
        return playerListForView;
    }

    public MutableLiveData<List<Team>> getTeamListForView() {
        if (teamListForView == null) {
            teamListForView = new MutableLiveData<>();
//            addNewPlayer();
        }
        return teamListForView;
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        teamListForView.setValue(event.getTeams());

        /*List<Pair<Player, Connection>> playerList = new ArrayList<>();
        Pair<Player, Connection> playerIntegerPair;
        for (int i = 0; i < event.getTeams().size(); i++) {
            for (int j = 0; j < event.getTeams().get(i).getTeamMembers().size(); j++) {
                playerIntegerPair = new Pair<>(event.getTeams().get(i).getTeamMembers().get(j), null); //todo fix
                playerList.add(playerIntegerPair);
            }
        }
        playerListForView.setValue(playerList);*/
    }

    public void setupNewHostSession() {
        repository.addNewPlayerToTeam("Im the Host", "iHost", "0");
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean isHost() {
        return isHost;
    }

    public void requestTeamChange(String teamId) {
        System.out.println("LobbyNetViewModel - requestTeamChange: Triggered!");

        if (teamListForView.getValue() != null) {
            repository.changeTeam(Objects.requireNonNull(teamListForView.getValue()).get(0).getTeamMembers().get(0), teamId); //todo make it smart to send local player auto-magically
        }
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

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}

