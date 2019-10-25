package com.god.kahit.networkManager.Packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a show round stats event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventShowRoundStatsPacket extends Packet {
    public static final int PACKET_ID = 17;

    public EventShowRoundStatsPacket() {
        super(PACKET_ID, new byte[0]);
    }
}
