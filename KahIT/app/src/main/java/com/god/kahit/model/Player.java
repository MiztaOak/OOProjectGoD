package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class Player { //todo revise with better use of access-modifiers. e.i not public everywhere
    private String id;
    private String name;
    private int score;
    private double scoreMultiplier;
    private int timeHeadstart;
    private int amountOfAlternatives;
    private boolean autoAnswer;
    private List<VanityItem> vanityItems;
    private boolean playerReady; //TODO check if this really is needed in the model since it should prob be in lobby
    private Item heldItem; //this item should be used when the player gets them maybe should be removed

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
        this.score = 0;
        this.playerReady = false;
        this.vanityItems = new ArrayList<>();
    }

    public void addVanityItem(VanityItem vanityItem) {
        vanityItems.add(vanityItem);
    }

    public String getHeldItemName() {
        return heldItem.getName();
    }

    public Item getHeldItem() {
        return heldItem;
    }

    /**
     * A method that sets the values of effects of a Modifier to players own values
     */
    public void setHeldItem(Modifier modifier) {
        this.scoreMultiplier = modifier.getScoreMultiplier();
        this.timeHeadstart = modifier.getTimeHeadstart();
        this.amountOfAlternatives = modifier.getAmountOfAlternatives();
        this.autoAnswer = modifier.isAutoAnswer();
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

    /**
     * A method that clears the effect of a Modifier after  it has been used
     */
    public void clearModifier() {
        this.scoreMultiplier = 1;
        this.timeHeadstart = 0;
        this.amountOfAlternatives = 0;
        this.autoAnswer = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


