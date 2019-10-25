package com.god.kahit.networkManager.packets;

/**
 * responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a lobby sync start event.
 * used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventLobbySyncStartPacket extends Packet {
    public static final int PACKET_ID = 1;

    public EventLobbySyncStartPacket(String targetPlayerId, String roomName, String gameModeId) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, roomName, gameModeId)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the targetPlayerId of a built EventLobbySyncStartPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventLobbySyncStartPacket
     * @return targetPlayerId string
     */
    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[0];
    }

    /**
     * Method used to parse the roomName of a built EventLobbySyncStartPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventLobbySyncStartPacket
     * @return roomName string
     */
    public static String getRoomName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[1];
    }

    /**
     * Method used to parse the gameModeId of a built EventLobbySyncStartPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventLobbySyncStartPacket
     * @return gameModeId string
     */
    public static String getGameModeId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[2];
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param targetPlayerId String containing the targetPlayerId
     * @param roomName       String containing the roomName
     * @param gameModeId     String containing the gameModeId
     * @return byte[] packet content
     */
    private byte[] createContent(String targetPlayerId, String roomName, String gameModeId) {
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
