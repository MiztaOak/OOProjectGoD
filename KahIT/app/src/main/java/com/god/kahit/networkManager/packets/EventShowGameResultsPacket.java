package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a show game results event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventShowGameResultsPacket extends Packet {
    public static final int PACKET_ID = 20;

    public EventShowGameResultsPacket() {
        super(PACKET_ID, new byte[0]);
    }
}
