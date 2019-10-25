package com.god.kahit.viewModel;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.god.kahit.repository.Repository;
import com.god.kahit.applicationEvents.LobbyNameChangeEvent;
import com.god.kahit.applicationEvents.MyPlayerIdChangedEvent;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.model.modelEvents.TeamChangeEvent;
import com.god.kahit.networkManager.Connection;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * responsibility: ViewModel for the LobbyNetView
 * <p>
 * used-by: LobbyNetView.
 *
 * @author Mats Cedervall
 */
public class LobbyNetViewModel extends ViewModel implements LifecycleObserver {
    private static final String TAG = LobbyNetViewModel.class.getSimpleName();
    private static final int MINIMUM_PLAYERS_FOR_GAME = 0; //set to 1 to disable solo-game
    private Repository repository;
    private MutableLiveData<List<Pair<Player, Connection>>> playerListForView;
    private MutableLiveData<List<Team>> teamListForView;
    private MutableLiveData<String> myPlayerId;
    private MutableLiveData<String> lobbyName;
    private boolean isHost;
    private boolean hasStartedGame;

    public LobbyNetViewModel() {
        repository = Repository.getInstance();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
        hasStartedGame = false;
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
            if (isHost) {
                myPlayerId.setValue(repository.getCurrentPlayer().getId());
            }
        }
        return myPlayerId;
    }

    public MutableLiveData<String> getLobbyName() {
        if (lobbyName == null) {
            lobbyName = new MutableLiveData<>();
            if (isHost) {
                lobbyName.setValue(repository.getRoomName());
            }
        }
        return lobbyName;
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

    @Subscribe
    public void onLobbyNameChangeEvent(LobbyNameChangeEvent event) {
        Log.d(TAG, String.format("onLobbyNameChangeEvent: event triggered, new name: '%s'", event.getLobbyName()));
        lobbyName.setValue(event.getLobbyName());
    }

    public void restoreNetInCommunication() {
        repository.restoreNetInCommunications();
    }

    /**
     * Method searches for the local player by matching Id's and returning the connectionPair.
     *
     * @return the connectionPair of the local player.
     */
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

    /**
     * Method searches for the team the local player is currently in and returns said team.
     *
     * @return - the Team for the local player.
     */
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

        sortListById(popTeams);

        return popTeams;
    }

    private void sortListById(List idList) {
        Collections.sort(idList, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
    }

    public void startHostBeacon() {
        repository.startHostBeacon();
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

    public boolean hasStartedGame() {
        return hasStartedGame;
    }

    public void setHasStartedGame(boolean hasStartedGame) {
        this.hasStartedGame = hasStartedGame;
    }

    public void startGame() {
        if (isHost) {
            repository.broadcastStartGame();
        }
        repository.startGame();
    }

    public void requestTeamChange(String teamId) {
        Log.d(TAG, "requestTeamChange: Triggered");
        repository.changeMyTeam(teamId, isHost);
    }

    public void requestSetReady(boolean isReady) {
        Log.d(TAG, "requestSetReady: Triggered");
        repository.setMyReadyStatus(isReady);
    }

    public void fireTeamChangeEvent() {
        Log.d(TAG, "fireTeamChangeEvent: Triggered");
        repository.fireTeamChangeEvent();
    }

    /**
     * Method checks if all players are ready to start the game.
     *
     * @return -true if all players are ready.
     */
    public boolean areAllPlayersReady() {
        boolean allAreReady = true;
        if (playerListForView.getValue() != null && playerListForView.getValue().size() > MINIMUM_PLAYERS_FOR_GAME) {
            for (Pair<Player, Connection> playerConnectionPair : playerListForView.getValue()) {
                if (!playerConnectionPair.first.isReady()) {
                    allAreReady = false;
                    break;
                }
            }
        } else {
            allAreReady = false; //Cant start game with no players
        }
        return allAreReady;
    }

    public void removePlayer(Player player) {
        repository.removePlayer(player);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: was called.");
    }
}

