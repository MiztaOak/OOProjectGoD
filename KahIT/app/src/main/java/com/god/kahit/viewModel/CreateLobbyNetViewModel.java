package com.god.kahit.viewModel;

import android.content.Context;

import com.god.kahit.Repository;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

public class CreateLobbyNetViewModel extends ViewModel implements LifecycleObserver {
    private static final String TAG = CreateLobbyNetViewModel.class.getSimpleName();
    private Repository repository;

    public CreateLobbyNetViewModel() {
        repository = Repository.getInstance();
    }

    public void setupNetwork(Context context) { //todo reset whole network, as much as possible
        repository.resetPlayerData();
        repository.createNewHostPlayer();
        repository.setupNetwork(context, true);
    }

    public String getRoomName() {
        return repository.getRoomName();
    }

    public void setRoomName(String roomName) {
        repository.setRoomName(roomName);
    }

    public String getPlayerName() {
        return repository.getHostPlayerName();
    }

    public void setPlayerName(String newPlayerName) {
        repository.setHostPlayerName(newPlayerName);
    }
}
