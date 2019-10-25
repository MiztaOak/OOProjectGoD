package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a team deleted event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventTeamDeletedPacket extends Packet {
    public static final int PACKET_ID = 14;

    public EventTeamDeletedPacket(String teamId) {
        super(PACKET_ID, (teamId).getBytes());
    }

    /**
     * Method used to parse the getTeamId of a built EventTeamDeletedPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventTeamDeletedPacket
     * @return getTeamId string
     */
    public static String getTeamId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
