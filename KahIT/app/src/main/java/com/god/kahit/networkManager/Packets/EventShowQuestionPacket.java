package com.god.kahit.networkManager.Packets;

public class EventShowQuestionPacket extends Packet {
    public static final int PACKET_ID = 16;

    public EventShowQuestionPacket(String categoryId, String questionId) {
        super(PACKET_ID, null);
        setPacketContent(createContent(categoryId, questionId)); //Super constructor must be called before anything else
    }

    public static String getCategoryId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String categoryId = content.split(";")[0];
        return categoryId;
    }

    public static String getQuestionId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String questionId = content.split(";")[1];
        return questionId;
    }

    private byte[] createContent(String categoryId, String questionId) { //todo maybe change to another separator than a semi-colon? \n?
        if (categoryId.contains(";")) { //If categoryId contains illegal characters throw error, as any modification to categoryId makes it useless
            throw new RuntimeException("categoryId contains illegal characters: " + categoryId);
        }
        if (questionId.contains(";")) { //If questionId contains illegal characters throw error, as any modification to questionId makes it useless
            throw new RuntimeException("questionId contains illegal characters: " + questionId);
        }

        return (categoryId + ";" + questionId).getBytes(); //Use semi-colon as separator
    }
}
