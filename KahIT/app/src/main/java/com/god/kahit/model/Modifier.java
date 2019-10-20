package com.god.kahit.model;

public class Modifier extends Item {
    private double scoreMultiplier;
    private int amountOfTime;

    public Modifier(int price, String name, double scoreMultiplier, int amountOfTime) {
        super(price, name);
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
