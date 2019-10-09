package com.god.kahit.model;

public class Player { //todo revise with better use of access-modifiers. e.i not public everywhere
    private final String id = "";  //TODO replace with good shit

    private String name;
    private int score;
    private Modifier currentEffects; //TODO replace with stats ask Johan if you don't remember how
    private boolean playerReady; //TODO check if this really is needed in the model since it should prob be in lobby
    private Item heldItem; //this item should be used when the player gets them maybe should be removed

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
        this.playerReady = false;
    }

    public Player() {
    }

    public String getHeldItemName() {
        return heldItem.getName();
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Item heldItem) {
        this.heldItem = heldItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateScore(int points) { //todo rename to addScore
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


