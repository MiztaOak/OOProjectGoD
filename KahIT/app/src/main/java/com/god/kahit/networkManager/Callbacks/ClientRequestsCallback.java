package com.god.kahit.networkManager.Callbacks;

import androidx.annotation.NonNull;

public abstract class ClientRequestsCallback {
    public ClientRequestsCallback() {
    }

    public abstract void onReceivedMyConnectionId(@NonNull String playerId);

    public abstract void onPlayerNameChangeRequest(@NonNull String targetPlayerId, @NonNull String newName);

    public abstract void onLobbyReadyChangeRequest(@NonNull String targetPlayerId, @NonNull boolean newState);

    public abstract void onTeamNameChangeRequest(@NonNull String teamId, @NonNull String newTeamName);

    public abstract void onPlayerTeamChangeRequest(@NonNull String targetPlayerId, @NonNull String newTeamId);

}
