package com.god.kahit.viewModel;

import com.god.kahit.model.Lobby;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

import static com.god.kahit.model.Lobby.BUS;

public class TeamArrangementViewModel {

    private static final String TAG = TeamArrangementViewModel.class.getSimpleName();

    private MutableLiveData<List<String>> playerMap;

    private Lobby lobby;

    public TeamArrangementViewModel(MutableLiveData<List<String>> playerMap) {
        this.playerMap = playerMap;
        BUS.register(this);
    }

    public MutableLiveData<List<String>> getPlayerMap() {
        if (playerMap == null) {
            playerMap = new MutableLiveData<>();
            addOnePlayer();
        }
        return playerMap;
    }

    public void addOnePlayer() {
        lobby.addPlayerToTeam(lobby.createNewPlayer(), lobby.getTeamList().size());
    }

    public void removeOnePlayer() {
        lobby.deleteTeam(lobby.getTeamList().size()); //TODO remove one player not a team.
    }

}
