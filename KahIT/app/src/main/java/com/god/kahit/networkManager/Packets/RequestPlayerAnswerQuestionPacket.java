package com.god.kahit.networkManager.Packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player answered question request.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class RequestPlayerAnswerQuestionPacket extends Packet {
    public static final int PACKET_ID = 25;

    public RequestPlayerAnswerQuestionPacket(String categoryId, String questionId, String givenAnswer, String timeLeft) {
        super(PACKET_ID, null);
        setPacketContent(createContent(categoryId, questionId, givenAnswer, timeLeft)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the categoryId of a built RequestPlayerAnswerQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a RequestPlayerAnswerQuestionPacket
     * @return categoryId string
     */
    public static String getCategoryId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String categoryId = content.split(";")[0];
        return categoryId;
    }

    /**
     * Method used to parse the questionId of a built RequestPlayerAnswerQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a RequestPlayerAnswerQuestionPacket
     * @return questionId string
     */
    public static String getQuestionId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String questionId = content.split(";")[1];
        return questionId;
    }

    /**
     * Method used to parse the givenAnswer of a built RequestPlayerAnswerQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a RequestPlayerAnswerQuestionPacket
     * @return givenAnswer string
     */
    public static String getGivenAnswer(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String givenAnswer = content.split(";")[2];
        return givenAnswer;
    }

    /**
     * Method used to parse the timeLeft of a built RequestPlayerAnswerQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a RequestPlayerAnswerQuestionPacket
     * @return timeLeft string
     */
    public static String getTimeLeft(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String timeLeft = content.split(";")[3];
        return timeLeft;
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param categoryId  String containing the categoryId
     * @param questionId  String containing the categoryId
     * @param givenAnswer String containing the categoryId
     * @param timeLeft    String containing the timeLeft of type long parsed to a string
     * @return byte[] packet content
     */
    private byte[] createContent(String categoryId, String questionId, String givenAnswer, String timeLeft) { //todo maybe change to another separator than a semi-colon? \n?
        if (categoryId.contains(";")) { //If categoryId contains illegal characters throw error, as any modification to categoryId makes it useless
            throw new RuntimeException("categoryId contains illegal characters: " + categoryId);
        }
        if (questionId.contains(";")) { //If questionId contains illegal characters throw error, as any modification to questionId makes it useless
            throw new RuntimeException("questionId contains illegal characters: " + questionId);
        }
        if (givenAnswer.contains(";")) { //If givenAnswer contains illegal characters throw error, as any modification to givenAnswer makes it useless
            throw new RuntimeException("givenAnswer contains illegal characters: " + givenAnswer);
        }

        timeLeft = timeLeft.replace(";", "");  //Ensures no semi-colons in timeLeft
        return (String.format("%s;%s;%s;%s", categoryId, questionId, givenAnswer, timeLeft)).getBytes(); //Use semi-colon as separator
    }

}
