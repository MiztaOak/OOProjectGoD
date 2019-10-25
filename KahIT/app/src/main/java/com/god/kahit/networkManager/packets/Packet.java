package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This abstract class is responsible for holding the content of a packet and
 * handling the common methods and attributes between all network packets.
 * @used-by: This class is used in the following classes:
 * PacketHandler, EventCategoryPlayerVotePacket, EventCategoryVoteResultPacket,
 * EventGameStartedPacket, EventLobbySyncEndPacket, EventLobbySyncStartPacket,
 * EventPlayerAnsweredQuestionPacket, EventPlayerChangeTeamPacket,
 * EventPlayerJoinedPacket, EventPlayerLeftPacket, EventPlayerNameChangePacket,
 * EventPlayerReadyChangePacket, EventShowCategorySelectionPacket, EventShowGameResultsPacket,
 * EventShowLotteryPacket, EventShowQuestionPacket, EventShowRoundStatsPacket,
 * EventTeamCreatedPacket, EventTeamDeletedPacket, EventTeamNameChangePacket,
 * RequestCategoryPlayerVotePacket, RequestPlayerAnswerQuestionPacket,
 * RequestPlayerChangeTeamPacket, RequestPlayerNameChangePacket, RequestPlayerReadyChangePacket,
 * RequestTeamNameChangePacket
 * @author: Mats Cedervall
 */
public abstract class Packet {
    private static final int MAX_ID_SIZE = 255;
    byte[] packetContent;
    private byte packetID;

    Packet(int packetID, byte[] packetContent) {
        this.packetID = verifyID(packetID);
        this.packetContent = packetContent;
    }

    /**
     * Method used to parse the raw incoming packet's content, not including the
     * overhead packetID byte
     *
     * @param rawPayload the byte[] to be parsed.
     * @return
     */
    public static byte[] getPayloadContent(byte[] rawPayload) {
        byte[] contentArr = new byte[rawPayload.length - 1];

        //Extract all payload bytes, but leaving out packetID
        for (int i = 0; i < contentArr.length; i++) {
            contentArr[i] = rawPayload[i + 1];
        }

        return contentArr;
    }

    /**
     * Method used to verify that the packet is not attempted to be constructed with an invalid
     * packetID
     *
     * @param id int to be used as packetID
     * @return
     */
    private byte verifyID(int id) {
        if (id > MAX_ID_SIZE) {
            throw new RuntimeException(String.format("Packet - verifyID: Attempted to " +
                    "initiate packet with a too large ID. Received: %s, max: %s", id, MAX_ID_SIZE));
        }
        return Byte.valueOf(String.valueOf(id)); //Make sure to parse as unsigned int
    }

    /**
     * Method used to retrieve the built byte[] packet, adding the overhead packetID to the byte[]
     *
     * @return byte[] the built packet content
     */
    public byte[] getBuiltPacket() {
        byte[] finalArr = new byte[1 + packetContent.length];
        finalArr[0] = packetID;

        //Append all payload bytes, leaving packetID at index 0
        for (int i = 0; i < packetContent.length; i++) {
            finalArr[i + 1] = packetContent[i];
        }

        return finalArr;
    }


    /**
     * Method used to set the packet content instead of passing it directly to the constructor
     *
     * @param packetContent byte[] containing all packet data
     */
    void setPacketContent(byte[] packetContent) {
        this.packetContent = packetContent;
    }
}
