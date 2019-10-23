package com.god.kahit.networkManager;

public class Connection {
    private String id;
    private String name;
    private ConnectionType type;
    private ConnectionState state;

    Connection(String id, String name, ConnectionType type, ConnectionState state) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public ConnectionType getType() {
        return type;
    }

    void setType(ConnectionType type) {
        this.type = type;
    }

    public ConnectionState getState() {
        return state;
    }

    void setState(ConnectionState state) {
        this.state = state;
    }

    @Override
    public boolean equals( Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (obj.getClass() != this.getClass()) {
            return false;
        } else if (((Connection) obj).getId().equals(id)) {
            return true;
        } else {
            return super.equals(obj);
        }
    }
}
