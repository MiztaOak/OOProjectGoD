package com.god.kahit.networkManager.Packets;

public class EventShowRoundStatsPacket extends Packet {
    public static final int PACKET_ID = 17;

    public EventShowRoundStatsPacket() {
        super(PACKET_ID, new byte[0]);
    }
}
