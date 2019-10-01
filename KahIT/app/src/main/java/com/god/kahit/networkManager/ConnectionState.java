package com.god.kahit.networkManager;

public enum ConnectionState {
    REJECTED,
    UNCONNECTED,
    DISCONNECTED,
    DISCONNECTING,
    CONNECTING,
    CONNECTED;

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

    public boolean isConnectable() {
        switch (this) {
            case REJECTED: //Is only possible from a host perspective, and a host may not request connections, only accept them. Thus not 'connectable'
                return false;
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