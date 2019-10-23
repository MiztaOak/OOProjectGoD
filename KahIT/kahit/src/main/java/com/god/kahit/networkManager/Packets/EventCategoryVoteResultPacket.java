package com.god.kahit.networkManager.Packets;

public class EventCategoryVoteResultPacket extends Packet {
    public static final int PACKET_ID = 23;

    public EventCategoryVoteResultPacket(String categoryId) {
        super(PACKET_ID, categoryId.getBytes());
    }

    public static String getCategoryId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
