package com.god.kahit.networkManager.Packets;

public class EventLobbySyncEndPacket extends Packet {
    public static final int PACKET_ID = 2;

    public EventLobbySyncEndPacket() {
        super(PACKET_ID, new byte[0]);
    }
}
