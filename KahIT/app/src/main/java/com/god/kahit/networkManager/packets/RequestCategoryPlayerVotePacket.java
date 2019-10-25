package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player category vote request.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class RequestCategoryPlayerVotePacket extends Packet {
    public static final int PACKET_ID = 22;

    public RequestCategoryPlayerVotePacket(String categoryId) {
        super(PACKET_ID, categoryId.getBytes());
    }

    /**
     * Method used to parse the categoryId of a built RequestCategoryPlayerVotePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a RequestCategoryPlayerVotePacket
     * @return categoryId string
     */
    public static String getCategoryId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
