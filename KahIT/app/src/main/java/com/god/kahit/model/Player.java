package com.god.kahit.model;


import java.util.List;

public class Player {
    private String name;
    private Integer score;
    private Modifier currentEffcts;
    private boolean playerReady; //todo to lobby
    private Item wonItem;

    public Player(String name, int score, List<Item> items) {
        this.name = name;
        this.score = score;
        this.playerReady = false;
    }

    public Player(String name, Integer score) {
        this.name = name;
        this.score = score;
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


