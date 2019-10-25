package com.god.kahit.networkManager.packets;

/**
 * responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a category vote result event.
 * used-by: This class is used in the following classes:
 * PacketHandler
 *
 * @author: Mats Cedervall
 */
public class EventCategoryVoteResultPacket extends Packet {
    public static final int PACKET_ID = 23;

    public EventCategoryVoteResultPacket(String categoryId) {
        super(PACKET_ID, categoryId.getBytes());
    }

    /**
     * Method used to parse the categoryId of a built EventCategoryVoteResultPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   *                   a EventCategoryVoteResultPacket
     * @return string containing the categoryId
     */
    public static String getCategoryId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
