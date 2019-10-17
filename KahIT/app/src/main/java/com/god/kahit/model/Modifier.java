package com.god.kahit.model;

public class Modifier extends Item {
    private double scoreMultiplier;
    private int amountOfTime;

    public Modifier(int price, String name, double scoreMultiplier, int amountOfTime, String id){
        super(price, name, id);
        this.scoreMultiplier = scoreMultiplier;
        this.amountOfTime = amountOfTime;
    }

    double getScoreMultiplier() {
        return scoreMultiplier;
    }

    int getAmountOfTime() {
        return amountOfTime;
    }
}
