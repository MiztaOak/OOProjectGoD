package com.god.kahit.networkManager.Packets;

import android.text.TextUtils;

public class EventShowCategorySelectionPacket extends Packet {
    public static final int PACKET_ID = 18;

    public EventShowCategorySelectionPacket(String[] categoryIds) {
        super(PACKET_ID, null);
        setPacketContent(createContent(categoryIds)); //Super constructor must be called before anything else
    }

    public static String[] getCategoryIds(byte[] rawPayload) {
        String content = new String(getPayloadContent(rawPayload));
        String[] categoryIds = content.split(";");
        return categoryIds;
    }

    private byte[] createContent(String[] categoryIds) { //todo maybe change to another separator than a semi-colon? \n?
        verifyContent(categoryIds);
        String extractedCategoryIds = TextUtils.join(";", categoryIds); //Use semi-colon as separator
        return extractedCategoryIds.getBytes();
    }

    private void verifyContent(String[] stringArrContent) {
        for (String s : stringArrContent) {
            if (s.contains(";")) {
                throw new RuntimeException("categoryIds contains illegal characters: " + s); //If categoryIds contains illegal characters throw error, as any modification to categoryIds makes it useless
            }
        }
    }
}
