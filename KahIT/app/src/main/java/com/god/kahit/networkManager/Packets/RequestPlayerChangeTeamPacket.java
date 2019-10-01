package com.god.kahit.networkManager.Packets;

public class RequestPlayerChangeTeamPacket extends Packet {
    public static final int PACKET_ID = 10;

    public RequestPlayerChangeTeamPacket(String newTeamId) {
        super(PACKET_ID, (newTeamId).getBytes());
    }

    public static String getNewTeamId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
