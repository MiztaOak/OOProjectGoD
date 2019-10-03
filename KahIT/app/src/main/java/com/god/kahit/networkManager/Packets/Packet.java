package com.god.kahit.networkManager.Packets;

public abstract class Packet {
    private static final int MAX_ID_SIZE = 255;
    byte[] packetContent;
    private byte packetID;

    Packet(int packetID, byte[] packetContent) {
        this.packetID = verifyID(packetID);
        this.packetContent = packetContent;
    }

    public static byte[] getPayloadContent(byte[] rawPayload) {
        byte[] contentArr = new byte[rawPayload.length - 1];

        //Extract all payload bytes, but leaving out packetID
        for (int i = 0; i < contentArr.length; i++) {
            contentArr[i] = rawPayload[i + 1];
        }

        return contentArr;
    }

    private byte verifyID(int id) {
        if (id > MAX_ID_SIZE) {
            throw new RuntimeException(String.format("Packet - verifyID: Attempted to initiate packet with a too large ID. Received: %s, max: %s", id, MAX_ID_SIZE));
        }
        return Byte.valueOf(String.valueOf(id)); //Make sure to parse as unsigned int
    }

    public byte[] getBuiltPacket() {
        byte[] finalArr = new byte[1 + packetContent.length];
        finalArr[0] = packetID;

        //Append all payload bytes, leaving packetID at index 0
        for (int i = 0; i < packetContent.length; i++) {
            finalArr[i + 1] = packetContent[i];
        }

        return finalArr;
    }

    void setPacketContent(byte[] packetContent) {
        this.packetContent = packetContent;
    }
}
