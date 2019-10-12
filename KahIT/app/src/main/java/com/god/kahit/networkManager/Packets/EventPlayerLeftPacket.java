package com.god.kahit.networkManager.Packets;

public class EventPlayerLeftPacket extends Packet {
    public static final int PACKET_ID = 10;

    public EventPlayerLeftPacket(String playerId) {
        super(PACKET_ID, playerId.getBytes());
    }

    public static String getPlayerId(byte[] rawPayload) {
        String playerId = new String(getPayloadContent(rawPayload));
        return playerId;
    }
}
