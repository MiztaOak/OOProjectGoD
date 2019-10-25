package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a team name change event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventTeamNameChangePacket extends Packet {
    public static final int PACKET_ID = 8;

    public EventTeamNameChangePacket(String teamId, String newTeamName) {
        super(PACKET_ID, null);
        setPacketContent(createContent(teamId, newTeamName)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the teamId of a built EventTeamNameChangePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventTeamNameChangePacket
     * @return teamId string
     */
    public static String getTeamId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[0];
    }

    /**
     * Method used to parse the newTeamName of a built EventTeamNameChangePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventTeamNameChangePacket
     * @return newTeamName string
     */
    public static String getNewTeamName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[1];
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param teamId      String containing the teamId
     * @param newTeamName String containing the newTeamName
     * @return byte[] packet content
     */
    private byte[] createContent(String teamId, String newTeamName) {
        if (teamId.contains(";")) { //If teamId contains illegal characters throw error, as any modification to teamId makes it useless
            throw new RuntimeException("TeamId contains illegal characters: " + teamId);
        }
        newTeamName = newTeamName.replace(";", ""); //Ensures no semi-colons in newTeamName
        return (teamId + ";" + newTeamName).getBytes(); //Use semi-colon as separator
    }
}
