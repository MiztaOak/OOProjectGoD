package com.god.kahit.networkManager.Packets;

public class EventPlayerChangeTeamPacket extends Packet {
    public static final int PACKET_ID = 12;

    public EventPlayerChangeTeamPacket(String targetPlayerId, String newTeamId) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, newTeamId)); //Super constructor must be called before anything else
    }

    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String targetPlayerId = content.split(";")[0];
        return targetPlayerId;
    }

    public static String getNewTeamId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String newTeamId = content.split(";")[1];
        return newTeamId;
    }

    private byte[] createContent(String targetPlayerId, String newTeamId) { //todo maybe change to another separator than a semi-colon? \n?
        if (targetPlayerId.contains(";")) { //If targetPlayerId contains illegal characters throw error, as any modification to targetPlayerId makes it useless
            throw new RuntimeException("targetPlayerId contains illegal characters: " + targetPlayerId);
        }
        if (newTeamId.contains(";")) { //If newTeamId contains illegal characters throw error, as any modification to newTeamId makes it useless
            throw new RuntimeException("newTeamId contains illegal characters: " + newTeamId);
        }

        return (targetPlayerId + ";" + newTeamId).getBytes(); //Use semi-colon as separator
    }
}
