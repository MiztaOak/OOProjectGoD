package com.god.kahit.viewModel;

import android.content.Context;

import com.god.kahit.model.GameMode;
import com.god.kahit.repository.NameGenerator;
import com.god.kahit.repository.Repository;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

/**
 * responsibility: ViewModel for the CreateLobbyNetView.
 * Handles the whole setup of a multiplayer lobby.
 * <p>
 * used-by: LobbyNetView.
 *
 * @author Mats Cedervall
 */
public class CreateLobbyNetViewModel extends ViewModel implements LifecycleObserver {
    private static final String TAG = CreateLobbyNetViewModel.class.getSimpleName();
    private final Repository repository;

    public CreateLobbyNetViewModel() {
        repository = Repository.getInstance();
    }

    public void setupNetwork(Context context) {
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

    public void setupNewGameInstance() {
        Repository.getInstance().setupNewGameInstance(GameMode.HOST);
    }

    public String generateNewPlayerName() {
        return NameGenerator.generatePlayerName();
    }

}
