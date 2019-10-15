package com.god.kahit.networkManager.Packets;

public class EventGameStartedPacket extends Packet {
    public static final int PACKET_ID = 15;

    public EventGameStartedPacket() {
        super(PACKET_ID, null);
    }
}
