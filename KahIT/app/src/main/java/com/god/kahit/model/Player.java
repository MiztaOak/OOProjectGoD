package com.god.kahit.model;


import java.util.List;

public class Player {
    private String name;
    private Integer score;
    private List<VanityItem> vanityItems;
    private Modifier currentEffcts;
    private boolean playerReady;
    private Item wonItem;
    private final String id;

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<VanityItem> getVanityItems() {
        return vanityItems;
    }

    public void addVanityItem(VanityItem vanityItems) {
        this.vanityItems.add(vanityItems);
    }

    public Modifier getCurrentEffcts() {
        return currentEffcts;
    }

    public void setCurrentEffcts(Modifier currentEffcts) {
        this.currentEffcts = currentEffcts;
    }

    public Player(String name, String id) {
        this.name = name;
        this.score = 0;
        this.id = id;
    }

    public String getWonItemName() {
        return wonItem.getName();
    }

    public Item getWonItem() {
        return wonItem;
    }

    public void setWonItem(Item wonItem) {
        this.wonItem = wonItem;
    }

    public void calculateNewScore(int newScore) {
        // todo switch instead
        if (currentEffcts.equals("double score")) {
            updateScore(newScore);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateScore(int points) {
        this.score += score;
    }

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


