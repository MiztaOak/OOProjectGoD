package com.god.kahit.networkManager.Packets;

public class PlayerIdPacket extends Packet {
    public static final int PACKET_ID = 0;

    public PlayerIdPacket(String playerId) {
        super(PACKET_ID, playerId.getBytes());
    }

    public static String getPlayerId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
