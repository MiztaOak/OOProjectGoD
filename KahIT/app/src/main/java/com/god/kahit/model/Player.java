package com.god.kahit.model;

import android.widget.ImageButton;

import java.util.List;

public class Player {
    private String name;
    private ImageButton selfie;
    private int score;
    private Item wonItem;
    private StatAlterer currentEffcts;


    public Player(String name, int score) {
        this.name = name;
        this.score = score;
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

    public Item getListItems() {
        return wonItem;
    }

    public void setWonItem(Item wonItem) {
        this.wonItem = wonItem;
    }
}




