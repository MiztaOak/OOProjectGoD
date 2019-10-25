package com.god.kahit.networkManager.Packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player ready change request.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class RequestPlayerReadyChangePacket extends Packet {
    public static final int PACKET_ID = 5;

    public RequestPlayerReadyChangePacket(boolean newState) {
        super(PACKET_ID, null);
        setPacketContent(createContent(newState)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the newState of a built RequestPlayerReadyChangePacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a RequestPlayerReadyChangePacket
     * @return newState boolean
     */
    public static boolean getNewState(byte[] rawPayload) {
        return (getPayloadContent(rawPayload)[0] == 1); //Parse rawPayload content, convert to boolean, return
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param state boolean representing the new player ready state
     * @return byte[] packet content
     */
    private byte[] createContent(boolean state) {
        byte[] contentArr = new byte[1];
        contentArr[0] = (state ? (byte) 1 : (byte) 0); //Convert boolean to byte 1/0
        return contentArr;
    }
}
