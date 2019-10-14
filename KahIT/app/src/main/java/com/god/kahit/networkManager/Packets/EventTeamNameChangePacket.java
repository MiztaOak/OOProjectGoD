package com.god.kahit.networkManager.Packets;

public class EventTeamNameChangePacket extends Packet {
    public static final int PACKET_ID = 8;

    public EventTeamNameChangePacket(String teamId, String newTeamName) {
        super(PACKET_ID, null);
        setPacketContent(createContent(teamId, newTeamName)); //Super constructor must be called before anything else
    }

    public static String getTeamId(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String teamId = content.split(";")[0];
        return teamId;
    }

    public static String getNewTeamName(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String newTeamName = content.split(";")[1];
        return newTeamName;
    }

    private byte[] createContent(String teamId, String newTeamName) { //todo maybe change to another separator than a semi-colon? \n?
        if (teamId.contains(";")) { //If teamId contains illegal characters throw error, as any modification to teamId makes it useless
            throw new RuntimeException("TeamId contains illegal characters: " + teamId);
        }
        newTeamName = newTeamName.replace(";", ""); //Ensures no semi-colons in newTeamName
        return (teamId + ";" + newTeamName).getBytes(); //Use semi-colon as separator
    }
}
