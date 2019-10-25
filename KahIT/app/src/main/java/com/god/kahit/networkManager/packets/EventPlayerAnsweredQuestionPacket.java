package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player answered question event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventPlayerAnsweredQuestionPacket extends Packet {
    public static final int PACKET_ID = 24;

    public EventPlayerAnsweredQuestionPacket(String targetPlayerId, String categoryId, String questionId, String givenAnswer, String timeLeft) {
        super(PACKET_ID, null);
        setPacketContent(createContent(targetPlayerId, categoryId, questionId, givenAnswer, timeLeft)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the targetPlayerId of a built EventPlayerAnsweredQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerAnsweredQuestionPacket
     * @return targetPlayerId string
     */
    public static String getTargetPlayerId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String targetPlayerId = content.split(";")[0];
        return targetPlayerId;
    }

    /**
     * Method used to parse the categoryId of a built EventPlayerAnsweredQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerAnsweredQuestionPacket
     * @return categoryId string
     */
    public static String getCategoryId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String categoryId = content.split(";")[1];
        return categoryId;
    }

    /**
     * Method used to parse the questionId of a built EventPlayerAnsweredQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerAnsweredQuestionPacket
     * @return questionId string
     */
    public static String getQuestionId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String questionId = content.split(";")[2];
        return questionId;
    }

    /**
     * Method used to parse the givenAnswer of a built EventPlayerAnsweredQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerAnsweredQuestionPacket
     * @return givenAnswer string
     */
    public static String getGivenAnswer(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String givenAnswer = content.split(";")[3];
        return givenAnswer;
    }

    /**
     * Method used to parse the timeLeft of a built EventPlayerAnsweredQuestionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventPlayerAnsweredQuestionPacket
     * @return timeLeft string
     */
    public static String getTimeLeft(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String timeLeft = content.split(";")[4];
        return timeLeft;
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param targetPlayerId String containing the targetPlayerId
     * @param categoryId     String containing the categoryId
     * @param questionId     String containing the questionId
     * @param givenAnswer    String containing the givenAnswer
     * @param timeLeft       String containing the timeLeft of type long parsed to a string
     * @return byte[] packet content
     */
    private byte[] createContent(String targetPlayerId, String categoryId, String questionId, String givenAnswer, String timeLeft) { //todo maybe change to another separator than a semi-colon? \n?
        if (targetPlayerId.contains(";")) { //If targetPlayerId contains illegal characters throw error, as any modification to targetPlayerId makes it useless
            throw new RuntimeException("targetPlayerId contains illegal characters: " + targetPlayerId);
        }
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
        return (String.format("%s;%s;%s;%s;%s", targetPlayerId, categoryId, questionId, givenAnswer, timeLeft)).getBytes(); //Use semi-colon as separator
    }
}
