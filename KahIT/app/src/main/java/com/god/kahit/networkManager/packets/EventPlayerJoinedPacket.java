package com.god.kahit.networkManager.packets;

/**
 * responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player joined game event.
 * used-by: This class is used in the following classes:
 * PacketHandler
 * author: Mats Cedervall
 */
public class EventPlayerJoinedPacket extends Packet {
    public static final int PACKET_ID = 9;

    public EventPlayerJoinedPacket(String playerId, String playerName) {
        super(PACKET_ID, null);
        setPacketContent(createContent(playerId, playerName)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the playerId of a built EventPlayerJoinedPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerJoinedPacket
     * @return playerId string
     */
    public static String getPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String playerId = content.split(";")[0];
        return playerId;
    }

    /**
     * Method used to parse the playerName of a built EventPlayerJoinedPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerJoinedPacket
     * @return playerName string
     */
    public static String getPlayerName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String playerName = content.split(";")[1];
        return playerName;
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param playerId   String containing the playerId
     * @param playerName String containing the playerName
     * @return byte[] packet content
     */
    private byte[] createContent(String playerId, String playerName) {
        if (playerId.contains(";")) { //If playerId contains illegal characters throw error, as any modification to playerId makes it useless
            throw new RuntimeException("playerId contains illegal characters: " + playerId);
        }
        playerName = playerName.replace(";", ""); //Ensures no semi-colons in playerName
        return (playerId + ";" + playerName).getBytes(); //Use semi-colon as separator
    }
}
