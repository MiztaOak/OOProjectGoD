package com.god.kahit.networkManager.Packets;

public class EventShowQuestionPacket extends Packet {
    public static final int PACKET_ID = 16;

    public EventShowQuestionPacket(String questionId) {
        super(PACKET_ID, questionId.getBytes());
    }

    public static String getQuestionId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload));
    }
}
