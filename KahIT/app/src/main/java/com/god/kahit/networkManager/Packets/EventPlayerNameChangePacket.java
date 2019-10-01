package com.god.kahit.networkManager.Packets;

public class EventPlayerNameChangePacket extends Packet {
    public static final int PACKET_ID = 3;

    public EventPlayerNameChangePacket(String targetPlayerId, String newPlayerName) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, newPlayerName)); //Super constructor must be called before anything else
    }

    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String playerId = content.split(";")[0];
        return playerId;
    }

    public static String getNewPlayerName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String newPlayerName = content.split(";")[1];
        return newPlayerName;
    }

    private byte[] createContent(String PlayerId, String newPlayerName) { //todo maybe change to another separator than a semi-colon? \n?
        if (PlayerId.contains(";")) { //If PlayerId contains illegal characters throw error, as any modification to PlayerId makes it useless
            throw new RuntimeException("PlayerId contains illegal characters: " + PlayerId);
        }
        newPlayerName = newPlayerName.replace(";", ""); //Ensures no semi-colons in newPlayerName
        return (PlayerId + ";" + newPlayerName).getBytes(); //Use semi-colon as separator
    }
}
