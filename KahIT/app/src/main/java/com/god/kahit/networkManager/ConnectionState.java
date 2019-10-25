package com.god.kahit.networkManager;

/**
 * responsibility: Enum that represents the different states that a connection can be in.
 * Also provides a method to check if a given connectionState is considered to be disconnected
 * used-by: This class is used in the following classes:
 * Connection, NetworkCallback, NetworkModule, Repository
 * @author: Mats Cedervall
 */
public enum ConnectionState {
    REJECTED,
    UNCONNECTED,
    DISCONNECTED,
    DISCONNECTING,
    CONNECTING,
    CONNECTED;

    /**
     * A method used to check if a given connection is considered to be disconnected
     *
     * @return boolean representing the check result
     */
    public boolean isDisconnected() {
        switch (this) {
            case REJECTED:
            case UNCONNECTED:
            case DISCONNECTED:
                return true;
            case DISCONNECTING:
            case CONNECTING:
            case CONNECTED:
                return false;
            default:
                throw new RuntimeException("Case not implemented");
        }
    }
}