package com.god.kahit.networkManager.Packets;

public class EventPlayerJoinedPacket extends Packet {
    public static final int PACKET_ID = 8;

    public EventPlayerJoinedPacket(String playerId, String playerName) {
        super(PACKET_ID, null);
        setPacketContent(createContent(playerId, playerName)); //Super constructor must be called before anything else
    }

    public static String getPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String playerId = content.split(";")[0];
        return playerId;
    }

    public static String getPlayerName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String playerName = content.split(";")[1];
        return playerName;
    }

    private byte[] createContent(String playerId, String playerName) { //todo maybe change to another separator than a semi-colon? \n?
        if (playerId.contains(";")) { //If playerId contains illegal characters throw error, as any modification to playerId makes it useless
            throw new RuntimeException("playerId contains illegal characters: " + playerId);
        }
        playerName = playerName.replace(";", ""); //Ensures no semi-colons in playerName
        return (playerId + ";" + playerName).getBytes(); //Use semi-colon as separator
    }
}
