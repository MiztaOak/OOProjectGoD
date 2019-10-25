package com.god.kahit.networkManager.Packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player change team event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventPlayerChangeTeamPacket extends Packet {
    public static final int PACKET_ID = 12;

    public EventPlayerChangeTeamPacket(String targetPlayerId, String newTeamId) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, newTeamId)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the targetPlayerId of a built EventPlayerChangeTeamPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerChangeTeamPacket
     * @return targetPlayerId string
     */
    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String targetPlayerId = content.split(";")[0];
        return targetPlayerId;
    }

    /**
     * Method used to parse the newTeamId of a built EventPlayerChangeTeamPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerChangeTeamPacket
     * @return newTeamId string
     */
    public static String getNewTeamId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String newTeamId = content.split(";")[1];
        return newTeamId;
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param targetPlayerId String containing the targetPlayerId
     * @param newTeamId      String containing the newTeamId
     * @return byte[] packet content
     */
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
