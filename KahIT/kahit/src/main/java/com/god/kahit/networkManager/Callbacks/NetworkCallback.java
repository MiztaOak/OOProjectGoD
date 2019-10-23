package com.god.kahit.networkManager.Callbacks;

import com.god.kahit.networkManager.Connection;
import com.god.kahit.networkManager.ConnectionState;

public abstract class NetworkCallback {
    public NetworkCallback() {
    }

    public abstract void onBytePayloadReceived( String id,  byte[] receivedBytes);

//    public abstract void onFilePayloadReceived( String id,  File receivedFile); //Not implemented //todo implement?

    public abstract void onHostFound( String id,  Connection connection);

    public abstract void onHostLost( String id);

    public abstract void onClientFound( String id,  Connection connection);

    public abstract void onConnectionEstablished( String id,  Connection connection);

    public abstract void onConnectionLost( String id);

    public abstract void onConnectionChanged( Connection connection,  ConnectionState oldState,  ConnectionState newState);

//    public abstract void onDisconnected( String id); //Not used, use onConnectionLost instead
}
