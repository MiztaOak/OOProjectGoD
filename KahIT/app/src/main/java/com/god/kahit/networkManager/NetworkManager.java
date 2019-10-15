package com.god.kahit.networkManager;

public interface NetworkManager {

    void startScan();

    void stopScan();

    void connectToHost(Connection connection);

    void startHostBeacon();

    void stopHostBeacon();

    void sendBytePayload(Connection connection, byte[] payload);

    void broadcastBytePayload(byte[] payload);

    void processPayloadQueue();

    void clearPayloadQueue();

    boolean isScanning();

    boolean isHostBeaconActive();

    boolean isHost();

    boolean isMe(String id);

//    ================

    void stopAllConnections();

    void cleanStop();

    int getConnectionsCount();

    Connection getConnectionHost();

    Connection getConnection(String id);

    Connection[] getConnections();

    void disconnect(String id);

    void disconnect(Connection connection);

    String getPlayerName();

    void setPlayerName(String name);

    String getPlayerId();

    void setPlayerId(String PlayerId);

    boolean isQueuingIncomingPayloads();

    void setQueueIncomingPayloads(boolean doQueue);
}
