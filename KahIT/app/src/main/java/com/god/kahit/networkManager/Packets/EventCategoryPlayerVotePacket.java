package com.god.kahit.networkManager.Packets;

public class EventCategoryPlayerVotePacket extends Packet {
    public static final int PACKET_ID = 21;

    public EventCategoryPlayerVotePacket(String targetPlayerId, String categoryId) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, categoryId)); //Super constructor must be called before anything else
    }

    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String targetPlayerId = content.split(";")[0];
        return targetPlayerId;
    }

    public static String getCategoryId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String categoryId = content.split(";")[1];
        return categoryId;
    }

    private byte[] createContent(String targetPlayerId, String categoryId) {
        if (targetPlayerId.contains(";")) { //If targetPlayerId contains illegal characters throw error, as any modification to targetPlayerId makes it useless
            throw new RuntimeException("targetPlayerId contains illegal characters: " + targetPlayerId);
        }
        if (categoryId.contains(";")) { //If categoryId contains illegal characters throw error, as any modification to categoryId makes it useless
            throw new RuntimeException("categoryId contains illegal characters: " + categoryId);
        }

        return (targetPlayerId + ";" + categoryId).getBytes(); //Use semi-colon as separator
    }
}
