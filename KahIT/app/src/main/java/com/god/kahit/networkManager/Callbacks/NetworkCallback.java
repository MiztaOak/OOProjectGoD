package com.god.kahit.networkManager.Callbacks;

import com.god.kahit.networkManager.Connection;
import com.god.kahit.networkManager.ConnectionState;

import androidx.annotation.NonNull;

public abstract class NetworkCallback {
    public NetworkCallback() {
    }

    public abstract void onBytePayloadReceived(@NonNull String id, @NonNull byte[] receivedBytes);

//    public abstract void onFilePayloadReceived(@NonNull String id, @NonNull File receivedFile); //Not implemented //todo implement?

    public abstract void onHostFound(@NonNull String id, @NonNull Connection connection);

    public abstract void onHostLost(@NonNull String id);

    public abstract void onClientFound(@NonNull String id, @NonNull Connection connection);

    public abstract void onConnectionEstablished(@NonNull String id, @NonNull Connection connection);

    public abstract void onConnectionLost(@NonNull String id);

    public abstract void onConnectionChanged(@NonNull Connection connection, @NonNull ConnectionState oldState, @NonNull ConnectionState newState);

//    public abstract void onDisconnected(@NonNull String id); //Not used, use onConnectionLost instead
}
