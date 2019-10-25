package com.god.kahit.networkManager.packets;

/**
 * responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a player left game event.
 * used-by: This class is used in the following classes:
 * PacketHandler
 * author: Mats Cedervall
 */
public class EventPlayerLeftPacket extends Packet {
    public static final int PACKET_ID = 10;

    public EventPlayerLeftPacket(String playerId) {
        super(PACKET_ID, playerId.getBytes());
    }

    /**
     * Method used to parse the newState of a built EventPlayerLeftPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   *                   a EventPlayerLeftPacket
     * @return string playerId
     */
    public static String getPlayerId(byte[] rawPayload) {
        String playerId = new String(getPayloadContent(rawPayload));
        return playerId;
    }
}
