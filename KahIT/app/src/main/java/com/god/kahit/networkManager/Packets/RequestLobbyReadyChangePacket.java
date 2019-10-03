package com.god.kahit.networkManager.Packets;

public class RequestLobbyReadyChangePacket extends Packet {
    public static final int PACKET_ID = 4;

    public RequestLobbyReadyChangePacket(boolean newState) {
        super(PACKET_ID, null);
        setPacketContent(createContent(newState)); //Super constructor must be called before anything else
    }

    public static boolean getNewState(byte[] rawPayload) {
        return (getPayloadContent(rawPayload)[0] == 1); //Parse rawPayload content, convert to boolean, return
    }

    private byte[] createContent(boolean state) {
        byte[] contentArr = new byte[1];
        contentArr[0] = (state ? (byte) 1 : (byte) 0); //Convert boolean to byte 1/0
        return contentArr;
    }
}
