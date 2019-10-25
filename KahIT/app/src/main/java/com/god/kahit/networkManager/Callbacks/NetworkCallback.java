package com.god.kahit.networkManager.Callbacks;

import com.god.kahit.networkManager.Connection;
import com.god.kahit.networkManager.ConnectionState;

import androidx.annotation.NonNull;

/**
 * Interface used to create callback methods for network connection and communication events
 * <p>
 * used by: NetworkManager, Repository
 *
 * @author Mats Cedervall
 */
public interface NetworkCallback {

    void onBytePayloadReceived(@NonNull String id, @NonNull byte[] receivedBytes);

//    void onFilePayloadReceived(@NonNull String id, @NonNull File receivedFile); //Not implemented

    void onHostFound(@NonNull String id, @NonNull Connection connection);

    void onHostLost(@NonNull String id);

    void onClientFound(@NonNull String id, @NonNull Connection connection);

    void onConnectionEstablished(@NonNull String id, @NonNull Connection connection);

    void onConnectionLost(@NonNull String id);

    void onConnectionChanged(@NonNull Connection connection, @NonNull ConnectionState oldState,
                             @NonNull ConnectionState newState);

//    void onDisconnected(@NonNull String id); //Not used, use onConnectionLost instead
}
