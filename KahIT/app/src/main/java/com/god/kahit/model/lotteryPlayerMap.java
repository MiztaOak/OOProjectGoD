package com.god.kahit.model;

import android.widget.ImageView;

public class lotteryPlayerMap {


    private BuyableItem playerWonItem; // buff or debuff
    private ImageView playerPic;
    private String playerName;



  /* public lotteryPlayerMap(ImageView playerPic, ImageView playerWonItem, TextView playerName){
        this.playerName = playerName;
        this.playerPic = playerPic;
        this.playerWonItem = playerWonItem;
    }*/

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


