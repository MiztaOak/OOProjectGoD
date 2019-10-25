package com.god.kahit.networkManager.packets;

/**
 * responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player category vote event.
 * used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventCategoryPlayerVotePacket extends Packet {
    public static final int PACKET_ID = 21;

    public EventCategoryPlayerVotePacket(String targetPlayerId, String categoryId) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, categoryId)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the targetPlayerId of a built EventCategoryPlayerVotePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventCategoryPlayerVotePacket
     * @return targetPlayerId string
     */
    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String targetPlayerId = content.split(";")[0];
        return targetPlayerId;
    }


    /**
     * Method used to parse the categoryId of a built EventCategoryPlayerVotePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventCategoryPlayerVotePacket
     * @return categoryId string
     */
    public static String getCategoryId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[1];
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param targetPlayerId String containing the targetPlayerId
     * @param categoryId     String containing the categoryId
     * @return byte[] packet content
     */
    private byte[] createContent(String targetPlayerId, String categoryId) {
        if (targetPlayerId.contains(";")) { //If targetPlayerId contains illegal characters throw error, as any modification to targetPlayerId makes it useless
            throw new RuntimeException("targetPlayerId contains illegal characters: " + targetPlayerId);
        }
        if (categoryId.contains(";")) { //If categoryId contains illegal characters throw error, as any modification to categoryId makes it useless
            throw new RuntimeException("categoryId contains illegal characters: " + categoryId);
        }

        return (targetPlayerId + ";" + categoryId).getBytes(); //Use semi-colon as separator
    }
}
