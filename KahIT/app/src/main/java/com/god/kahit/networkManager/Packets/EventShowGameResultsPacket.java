package com.god.kahit.networkManager.Packets;

public class EventShowGameResultsPacket extends Packet {
    public static final int PACKET_ID = 20;

    public EventShowGameResultsPacket() {
        super(PACKET_ID, new byte[0]);
    }
}
