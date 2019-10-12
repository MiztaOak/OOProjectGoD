package com.god.kahit.networkManager.Packets;

public class EventLobbySyncStartPacket extends Packet {
    public static final int PACKET_ID = 1;

    public EventLobbySyncStartPacket(String targetPlayerId, String roomName, String gameModeId) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, roomName, gameModeId)); //Super constructor must be called before anything else
    }

    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String targetPlayerId = content.split(";")[0];
        return targetPlayerId;
    }

    public static String getRoomName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String roomName = content.split(";")[1];
        return roomName;
    }

    public static String getGameModeId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String gameModeId = content.split(";")[2];
        return gameModeId;
    }

    private byte[] createContent(String targetPlayerId, String roomName, String gameModeId) { //todo maybe change to another separator than a semi-colon? \n?
        if (targetPlayerId.contains(";")) { //If targetPlayerId contains illegal characters throw error, as any modification to targetPlayerId makes it useless
            throw new RuntimeException("targetPlayerId contains illegal characters: " + targetPlayerId);
        }

        if (gameModeId.contains(";")) { //If gameModeId contains illegal characters throw error, as any modification to gameModeId makes it useless
            throw new RuntimeException("gameModeId contains illegal characters: " + gameModeId);
        }

        roomName = roomName.replace(";", ""); //Ensures no semi-colons in roomName
        return (targetPlayerId + ";" + roomName + ";" + gameModeId).getBytes(); //Use semi-colon as separator
    }
}
