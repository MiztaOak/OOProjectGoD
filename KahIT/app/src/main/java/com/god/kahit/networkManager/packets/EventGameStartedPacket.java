package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a game started event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventGameStartedPacket extends Packet {
    public static final int PACKET_ID = 15;

    public EventGameStartedPacket() {
        super(PACKET_ID, new byte[0]);
    }
}
