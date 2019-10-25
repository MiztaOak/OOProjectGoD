package com.god.kahit.networkManager.Packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey the connected endpoint id event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class PlayerIdPacket extends Packet {
    public static final int PACKET_ID = 0;

    public PlayerIdPacket(String playerId) {
        super(PACKET_ID, playerId.getBytes());
    }

    /**
     * Method used to parse the playerId of a built PlayerIdPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a PlayerIdPacket
     * @return playerId string
     */
    public static String getPlayerId(byte[] rawPayload) {
        return new String(getPayloadContent(rawPayload)); //Parse rawPayload content, convert to string, return
    }
}
