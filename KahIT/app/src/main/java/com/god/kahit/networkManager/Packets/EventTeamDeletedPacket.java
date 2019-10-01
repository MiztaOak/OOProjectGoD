package com.god.kahit.networkManager.Packets;

public class EventTeamDeletedPacket extends Packet {
    public static final int PACKET_ID = 13;

    public EventTeamDeletedPacket(String teamId) {
        super(PACKET_ID, (teamId).getBytes());
    }

    public static String getTeamId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
