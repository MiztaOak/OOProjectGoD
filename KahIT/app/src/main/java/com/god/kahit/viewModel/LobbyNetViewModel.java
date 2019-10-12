package com.god.kahit.viewModel;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.god.kahit.Events.MyPlayerIdChangedEvent;
import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.Repository;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.networkManager.Connection;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class LobbyNetViewModel extends ViewModel implements LifecycleObserver {
    private static final String TAG = LobbyNetViewModel.class.getSimpleName();
    private Repository repository;
    private MutableLiveData<List<Pair<Player, Connection>>> playerListForView;
    private MutableLiveData<List<Team>> teamListForView;
    private MutableLiveData<String> myPlayerId;
    private boolean isHost;

    public LobbyNetViewModel() {
        repository = Repository.getInstance();
        BUS.register(this);
    }

    public MutableLiveData<List<Pair<Player, Connection>>> getPlayerListForView() {
        if (playerListForView == null) {
            playerListForView = new MutableLiveData<>();
        }
        return playerListForView;
    }

    public MutableLiveData<List<Team>> getTeamListForView() {
        if (teamListForView == null) {
            teamListForView = new MutableLiveData<>();
        }
        return teamListForView;
    }

    public MutableLiveData<String> getMyPlayerId() {
        if (myPlayerId == null) {
            myPlayerId = new MutableLiveData<>();
        }
        return myPlayerId;
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        List<Team> teams = event.getTeams();
        teams = filterPopulatedTeams(teams);
        teamListForView.setValue(teams);

        updatePlayerList();
    }

    @Subscribe
    public void onMyPlayerIdChangedEvent(MyPlayerIdChangedEvent event) {
        Log.d(TAG, String.format("onMyPlayerIdChangedEvent: event triggered, new id: '%s'", event.getNewPlayerId()));
        myPlayerId.setValue(event.getNewPlayerId());
    }

    public Pair<Player, Connection> getMe() {
        if (myPlayerId.getValue() == null) {
            Log.d(TAG, "getMe: myPlayerId.getValue() == null, could therefore not find me - returning null");
            return null;
        }

        if (playerListForView.getValue() == null) {
            Log.d(TAG, "getMe: playerListForView.getValue == null, could therefore not find me - returning null");
            return null;
        }

        for (Pair<Player, Connection> playerConnectionPair : playerListForView.getValue()) {
            if (playerConnectionPair.first.getId().equals(myPlayerId.getValue())) {
                return playerConnectionPair;
            }
        }

        Log.d(TAG, "getMe: myId could not be found inside playerList, returning null");
        return null;
    }

    public Team getMyTeam() {
        Pair<Player, Connection> myPlayerConnectionPair = getMe();
        if (myPlayerConnectionPair == null) {
            Log.d(TAG, "getMyTeam: myPlayerConnectionPair == null, could therefore not find my team - returning null");
            return null;
        }

        if (teamListForView.getValue() == null) {
            Log.d(TAG, "getMyTeam: teamListForView.getValue() == null, could therefore not find my team - returning null");
            return null;
        }

        for (Team team : teamListForView.getValue()) {
            for (Player teamMember : team.getTeamMembers()) {
                if (myPlayerConnectionPair.first.getId().equals(teamMember.getId())) {
                    return team;
                }
            }
        }

        Log.d(TAG, "getMyTeam: my playerId was not found in any team's playerList - returning null");
        return null;
    }

    private void updatePlayerList() {
        List<Team> teams = teamListForView.getValue();
        if (teams != null) {
            List<Pair<Player, Connection>> players = new ArrayList<>();

            for (Team team : teams) {
                for (Player teamMember : team.getTeamMembers()) {
                    Connection con = repository.getConnection(teamMember.getId());
                    Pair<Player, Connection> playerConPair = new Pair<>(teamMember, con);
                    players.add(playerConPair);
                }
            }

            playerListForView.setValue(players);
        } else {
            Log.d(TAG, "updatePlayerList: teamListForView.getValue == null, skipping updatePlayerList");
        }
    }

    private List<Team> filterPopulatedTeams(List<Team> teams) {
        List<Team> popTeams = new ArrayList<>();
        for (Team team : teams) {
            if (team.getTeamMembers().size() > 0) {
                popTeams.add(team);
                Log.d(TAG, String.format("filterPopulatedTeams: added populated team: '%s'", team.getTeamName()));
            } else {
                Log.d(TAG, String.format("filterPopulatedTeams: removed unpopulated team: '%s'", team.getTeamName()));
            }
        }
        return popTeams;
    }

    public void setupNewLobbySession(Context context) {
        if (isHost) {
            repository.createNewHostPlayer();
        }
        repository.setupNetwork(context, isHost);
        if (isHost) {
            repository.startHostBeacon();
        }
    }

    public void stopHostBeacon() {
        repository.stopHostBeacon();
    }

    public void clearConnections() {
        repository.clearConnections();
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean isHost() {
        return isHost;
    }

    public void requestTeamChange(String teamId) {
        System.out.println("LobbyNetViewModel - requestTeamChange: Triggered!");
        repository.requestChangeMyTeam(teamId, isHost);
    }

    public boolean areAllPlayersReady() {
        boolean allAreReady = true;
        if (playerListForView.getValue() != null && playerListForView.getValue().size() > 0) {
            for (Pair<Player, Connection> playerConnectionPair : playerListForView.getValue()) {
                if (!playerConnectionPair.first.isPlayerReady()) {
                    allAreReady = false;
                    break;
                }
            }
        } else {
            allAreReady = false; //Cant start game with no players
        }
        return allAreReady;
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

