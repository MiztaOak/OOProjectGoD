package com.god.kahit.networkManager.Packets;

import java.util.Arrays;

public class EventLobbyReadyChangePacket extends Packet {
    public static final int PACKET_ID = 5;

    public EventLobbyReadyChangePacket(String targetPlayerId, boolean newState) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, newState)); //Super constructor must be called before anything else
    }

    public static String getTargetPlayerId(byte[] rawPayload) {
        byte[] rawContent = getPayloadContent(rawPayload);
        String playerId = new String(Arrays.copyOfRange(rawContent, 1, rawContent.length)); //Leave out newState-byte, copy rest to another array. Convert this new array to string, return
        return playerId;
    }

    public static boolean getNewState(byte[] rawPayload) {
        return (getPayloadContent(rawPayload)[0] == 1); //Parse rawPayload content, convert to boolean, return
    }

    private byte[] createContent(String targetPlayerId, boolean state) {
        byte[] targetPlayerIdBytes = targetPlayerId.getBytes();
        byte[] contentArr = new byte[1 + targetPlayerIdBytes.length];
        contentArr[0] = (state ? (byte) 1 : (byte) 0); //Convert boolean to byte 1/0

        for (int i = 0; i < targetPlayerIdBytes.length; i++) {
            contentArr[i + 1] = targetPlayerIdBytes[i]; //Appends targetPlayerIdBytes after first byte, e.i the newState byte
        }

        return contentArr;
    }
}
