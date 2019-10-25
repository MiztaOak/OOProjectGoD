package com.god.kahit.networkManager.Packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a team created event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventTeamCreatedPacket extends Packet {
    public static final int PACKET_ID = 13;

    public EventTeamCreatedPacket(String newTeamId, String newTeamName) {
        super(PACKET_ID, null);
        setPacketContent(createContent(newTeamId, newTeamName)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the newTimeId of a built EventTeamCreatedPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventTeamCreatedPacket
     * @return newTimeId string
     */
    public static String getNewTeamId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String newTeamId = content.split(";")[0];
        return newTeamId;
    }

    /**
     * Method used to parse the newTeamName of a built EventTeamCreatedPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventTeamCreatedPacket
     * @return newTeamName string
     */
    public static String getNewTeamName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String newTeamName = content.split(";")[1];
        return newTeamName;
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param newTeamId   String containing the categoryId
     * @param newTeamName String containing the categoryId
     * @return byte[] packet content
     */
    private byte[] createContent(String newTeamId, String newTeamName) { //todo maybe change to another separator than a semi-colon? \n?
        if (newTeamId.contains(";")) { //If newTeamId contains illegal characters throw error, as any modification to newTeamId makes it useless
            throw new RuntimeException("newTeamId contains illegal characters: " + newTeamId);
        }

        newTeamName = newTeamName.replace(";", ""); //Ensures no semi-colons in newTeamName
        return (newTeamId + ";" + newTeamName).getBytes(); //Use semi-colon as separator
    }
}
