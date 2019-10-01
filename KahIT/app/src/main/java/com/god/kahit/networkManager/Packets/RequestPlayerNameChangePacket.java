package com.god.kahit.networkManager.Packets;

public class RequestPlayerNameChangePacket extends Packet {
    public static final int PACKET_ID = 2;

    public RequestPlayerNameChangePacket(String newPlayerName) {
        super(PACKET_ID, newPlayerName.getBytes());
    }

    public static String getNewPlayerName(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
