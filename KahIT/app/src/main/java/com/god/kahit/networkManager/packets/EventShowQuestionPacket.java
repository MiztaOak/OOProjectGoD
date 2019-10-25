package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a show question event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventShowQuestionPacket extends Packet {
    public static final int PACKET_ID = 16;

    public EventShowQuestionPacket(String categoryId, String questionId) {
        super(PACKET_ID, null);
        setPacketContent(createContent(categoryId, questionId)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the categoryId of a built EventShowQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventShowQuestionPacket
     * @return categoryId string
     */
    public static String getCategoryId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[0];
    }

    /**
     * Method used to parse the questionId of a built EventShowQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventShowQuestionPacket
     * @return questionId string
     */
    public static String getQuestionId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";")[1];
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param categoryId String containing the categoryId
     * @param questionId String containing the questionId
     * @return byte[] packet content
     */
    private byte[] createContent(String categoryId, String questionId) {
        if (categoryId.contains(";")) { //If categoryId contains illegal characters throw error, as any modification to categoryId makes it useless
            throw new RuntimeException("categoryId contains illegal characters: " + categoryId);
        }
        if (questionId.contains(";")) { //If questionId contains illegal characters throw error, as any modification to questionId makes it useless
            throw new RuntimeException("questionId contains illegal characters: " + questionId);
        }

        return (categoryId + ";" + questionId).getBytes(); //Use semi-colon as separator
    }
}
