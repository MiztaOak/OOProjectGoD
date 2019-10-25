package com.god.kahit.networkManager.packets;

import android.text.TextUtils;

/**
 * @responsibility: This class is responsible for building and parsing the necessary contents
 * to convey a category selection event.
 * @used-by: This class is used in the following classes:
 * PacketHandler
 * @author: Mats Cedervall
 */
public class EventShowCategorySelectionPacket extends Packet {
    public static final int PACKET_ID = 18;

    public EventShowCategorySelectionPacket(String[] categoryIds) {
        super(PACKET_ID, null);
        setPacketContent(createContent(categoryIds)); //Super constructor must be called before anything else
    }

    /**
     * Method used to parse the categoryId of a built EventShowCategorySelectionPacket
     *
     * @param rawPayload byte[] containing the packetID and the packet specific content of
     *                   a EventShowCategorySelectionPacket
     * @return categoryId string
     */
    public static String[] getCategoryIds(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        return content.split(";");
    }

    /**
     * Method used to create the packet specific byte[] content
     *
     * @param categoryIds String containing the targetPlayerId
     * @return byte[] packet content
     */
    private byte[] createContent(String[] categoryIds) {
        verifyContent(categoryIds);
        String extractedCategoryIds = TextUtils.join(";", categoryIds); //Use semi-colon as separator
        return extractedCategoryIds.getBytes();
    }

    /**
     * Method to verify that the content does not contain semi-colons, as these are used
     * as text separators. If any semi-colon is found, it throws a runtime exception.
     *
     * @param stringArrContent String[] containing the categoryId's
     */
    private void verifyContent(String[] stringArrContent) {
        for (String s : stringArrContent) {
            if (s.contains(";")) {
                throw new RuntimeException("categoryIds contains illegal characters: " + s); //If categoryIds contains illegal characters throw error, as any modification to categoryIds makes it useless
            }
        }
    }
}
