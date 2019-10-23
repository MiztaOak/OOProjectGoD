package com.god.kahit.networkManager.Packets;

public class EventTeamCreatedPacket extends Packet {
    public static final int PACKET_ID = 13;

    public EventTeamCreatedPacket(String newTeamId, String newTeamName) {
        super(PACKET_ID, null);
        setPacketContent(createContent(newTeamId, newTeamName)); //Super constructor must be called before anything else
    }

    public static String getNewTeamId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String newTeamId = content.split(";")[0];
        return newTeamId;
    }

    public static String getNewTeamName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String newTeamName = content.split(";")[1];
        return newTeamName;
    }

    private byte[] createContent(String newTeamId, String newTeamName) { //todo maybe change to another separator than a semi-colon? \n?
        if (newTeamId.contains(";")) { //If newTeamId contains illegal characters throw error, as any modification to newTeamId makes it useless
            throw new RuntimeException("newTeamId contains illegal characters: " + newTeamId);
        }

        newTeamName = newTeamName.replace(";", ""); //Ensures no semi-colons in newTeamName
        return (newTeamId + ";" + newTeamName).getBytes(); //Use semi-colon as separator
    }
}
