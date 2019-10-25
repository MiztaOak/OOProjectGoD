package com.god.kahit.networkManager.packets;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a lobby sync end event.
 * @used-by: This class is used in the following classes:
 * NetworkModule, PacketHandler
 * @author: Mats Cedervall
 */
public class EventLobbySyncEndPacket extends Packet {
    public static final int PACKET_ID = 2;

    public EventLobbySyncEndPacket() {
        super(PACKET_ID, new byte[0]);
    }
}
