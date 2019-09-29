package com.god.kahit.model;

import android.widget.ImageView;

public class lotteryItem {

    private BuyableItem playerWonItem; // buff or debuff
    private ImageView playerPic;
    private String playerName;


    public BuyableItem getPlayerWonItem() {
        return playerWonItem;
    }

    public void setPlayerWonItem(BuyableItem playerWonItem) {
        this.playerWonItem = playerWonItem;
    }

    public ImageView getPlayerImage() {
        return playerPic;
    }

    public void setPlayerImage(ImageView playerPic) {
        this.playerPic = playerPic;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}


