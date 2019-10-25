package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player change team request.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class RequestPlayerChangeTeamPacket extends Packet {
    public static final int PACKET_ID = 11;

    public RequestPlayerChangeTeamPacket(String newTeamId) {
        super(PACKET_ID, newTeamId.getBytes());
    }

    /**
     * Method used to parse the newTimeId of a built RequestPlayerChangeTeamPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a RequestPlayerChangeTeamPacket
     * @return newTimeId string
     */
    public static String getNewTeamId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
