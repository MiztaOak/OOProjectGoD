package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player change name request.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class RequestPlayerNameChangePacket extends Packet {
    public static final int PACKET_ID = 3;

    public RequestPlayerNameChangePacket(String newPlayerName) {
        super(PACKET_ID, newPlayerName.getBytes());
    }

    /**
     * Method used to parse the newPlayerName of a built RequestPlayerNameChangePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a RequestPlayerNameChangePacket
     * @return newPlayerName string
     */
    public static String getNewPlayerName(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
