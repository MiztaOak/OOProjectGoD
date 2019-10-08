package com.god.kahit.model;


import java.util.List;

public class Player {
    private final String id = "";  //TODO replace with good shit

    private String name;
    private Integer score;
    private Modifier currentEffcts; //TODO replace with stats ask Johan if you don't remember how
    private boolean playerReady; //TODO check if this really is needed in the model since it should prob be in lobby
    private Item wonItem; //this item should be used when the player gets them maybe should be removed

    public Player(String name, Integer score) {
        this.name = name;
        this.score = score;
        this.playerReady = false;
    }


    public void setWonItem(Item wonItem) {
        this.wonItem = wonItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateScore(int points) { //todo
        this.score += score;
    } //TODO add calculation that takes current buff into account

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPlayerReady() {
        return playerReady;
    }

    public void setPlayerReady(boolean playerReady) {
        this.playerReady = playerReady;
    }
}


