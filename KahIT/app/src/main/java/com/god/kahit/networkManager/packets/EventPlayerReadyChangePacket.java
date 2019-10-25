package com.god.kahit.networkManager.packets;

import java.util.Arrays;

/**
 * responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player ready change event.
 * used-by: This class is used in the following classes:
 * PacketHandler
 * author: Mats Cedervall
 */
public class EventPlayerReadyChangePacket extends Packet {
    public static final int PACKET_ID = 6;

    public EventPlayerReadyChangePacket(String targetPlayerId, boolean newState) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, newState)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the targetPlayerId of a built EventPlayerReadyChangePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerReadyChangePacket
     * @return targetPlayerId string
     */
    public static String getTargetPlayerId(byte[] rawPayload) {
        byte[] rawContent = getPayloadContent(rawPayload);
        String playerId = new String(Arrays.copyOfRange(rawContent, 1, rawContent.length)); //Leave out newState-byte, copy rest to another array. Convert this new array to string, return
        return playerId;
    }

    /**
     * Method used to parse the newState of a built EventPlayerReadyChangePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerReadyChangePacket
     * @return newState boolean
     */
    public static boolean getNewState(byte[] rawPayload) {
        return (getPayloadContent(rawPayload)[0] == 1); //Parse rawPayload content, convert to boolean, return
    }


    /**
     * Method used to create the packet specific byte[] content
     *
     * @param targetPlayerId String containing the targetPlayerId
     * @param state          boolean representing the ready state
     * @return byte[] packet content
     */
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
