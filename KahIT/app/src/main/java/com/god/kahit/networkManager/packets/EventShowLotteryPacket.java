package com.god.kahit.networkManager.packets;

import android.text.TextUtils;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a category selection event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventShowLotteryPacket extends Packet {
    public static final int PACKET_ID = 19;

    public EventShowLotteryPacket(String[][] playersWonItemsMatrix) {
        super(PACKET_ID, null);
        setPacketContent(createContent(playersWonItemsMatrix)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the playersWonItemsMatrix of a built EventShowLotteryPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventShowLotteryPacket
     * @return playersWonItemsMatrix String[][]
     */
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

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param playersWonItemsMatrix String[][] containing each player and what item it has won
     * @return byte[] packet content
     */
    private byte[] createContent(String[][] playersWonItemsMatrix) {
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

    /**
     * Method to verify that the content does not contain semi-colons, as these are used
     * as text separators. If any semi-colon is found, it throws a runtime exception.
     *
     * @param stringMatrixContent String[][] containing each player and what item it has won
     */
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
