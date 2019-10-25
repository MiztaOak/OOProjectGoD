package com.god.kahit.networkManager.packets;

/**
 * responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player name change event.
 * used-by: This class is used in the following classes:
 * PacketHandler
 * author: Mats Cedervall
 */
public class EventPlayerNameChangePacket extends Packet {
    public static final int PACKET_ID = 4;

    public EventPlayerNameChangePacket(String targetPlayerId, String newPlayerName) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, newPlayerName)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the targetPlayerId of a built EventPlayerNameChangePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerNameChangePacket
     * @return targetPlayerId string
     */
    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[0];
    }

    /**
     * Method used to parse the newPlayerName of a built EventPlayerNameChangePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerNameChangePacket
     * @return newPlayerName string
     */
    public static String getNewPlayerName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[1];
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param PlayerId      String containing the playerId
     * @param newPlayerName String containing the newPlayerName
     * @return byte[] packet content
     */
    private byte[] createContent(String PlayerId, String newPlayerName) {
        if (PlayerId.contains(";")) { //If PlayerId contains illegal characters throw error, as any modification to PlayerId makes it useless
            throw new RuntimeException("PlayerId contains illegal characters: " + PlayerId);
        }
        newPlayerName = newPlayerName.replace(";", ""); //Ensures no semi-colons in newPlayerName
        return (PlayerId + ";" + newPlayerName).getBytes(); //Use semi-colon as separator
    }
}
