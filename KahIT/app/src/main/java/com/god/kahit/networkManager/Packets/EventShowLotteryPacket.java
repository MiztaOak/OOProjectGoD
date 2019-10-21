package com.god.kahit.networkManager.Packets;

import android.text.TextUtils;

public class EventShowLotteryPacket extends Packet {
    public static final int PACKET_ID = 19;

    public EventShowLotteryPacket(String[][] playersWonItemsMatrix) { //todo expected to contain playerWonItemsMatrix[1] = {playerId, wonItemId}
        super(PACKET_ID, null);
        setPacketContent(createContent(playersWonItemsMatrix)); //Super constructor must be called before anything else
    }

    public static String[][] getPlayersWonItemsMatrix(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String[] playersWonItems = content.split(";");
        String[][] playerWonItemsMatrix = new String[playersWonItems.length / 2][2];

        for (int i = 0; i < playerWonItemsMatrix.length; i++) {
            playerWonItemsMatrix[i][0] = playersWonItems[i * 2]; //playerId
            playerWonItemsMatrix[i][1] = playersWonItems[i * 2 + 1]; //wonItemId
        }

        return playerWonItemsMatrix;
    }

    private byte[] createContent(String[][] playersWonItemsMatrix) { //todo maybe change to another separator than a semi-colon? \n?
        verifyContent(playersWonItemsMatrix);
        String[] playersWonItemsArr = new String[playersWonItemsMatrix.length];
        for (int i = 0; i < playersWonItemsMatrix.length; i++) {
            //Construct a array looking like: {playerId;wonItemId, playerId;wonItemId,...}
            playersWonItemsArr[i] = TextUtils.join(";", playersWonItemsMatrix[i]); //Use semi-colon as separator
        }

        //Construct a string looking like: "playerId;wonItemId;playerId;wonItemId;..."
        String extractedPlayersWonItems = TextUtils.join(";", playersWonItemsArr); //Use semi-colon as separator
        return extractedPlayersWonItems.getBytes();
    }

    private void verifyContent(String[][] stringMatrixContent) {
        for (String[] strings : stringMatrixContent) {
            for (String string : strings) {
                if (string.contains(";")) {
                    throw new RuntimeException("playersWonItemsMatrix contains " +
                            "illegal characters: " + string);
                    //If playersWonItemsMatrix contains illegal characters throw error,
                    // as any modification to playersWonItemsMatrix makes it useless
                }
            }
        }
    }
}
