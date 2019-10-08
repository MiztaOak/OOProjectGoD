package com.god.kahit.model;

public class Modifier extends Item {
    private double scoreMultiplier;
    private int timeHeadstart;
    private int amountOfAlternatives;
    //TODO add the last of the values like robot boolean

    public Modifier(int price, String type, String name, String imageSource, int scoreMultiplier, int timeHeadstart, int amountOfAlternatives){
        super(price, type, name, imageSource);
        this.scoreMultiplier = scoreMultiplier;
        this.timeHeadstart = timeHeadstart;
        this.amountOfAlternatives = amountOfAlternatives;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public void setScoreMultiplier(double scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    public int getTimeHeadstart() {
        return timeHeadstart;
    }

    public void setTimeHeadstart(int timeHeadstart) {
        this.timeHeadstart = timeHeadstart;
    }

    public int getAmountOfAlternatives() {
        return amountOfAlternatives;
    }

    public void setAmountOfAlternatives(int amountOfAlternatives) {
        this.amountOfAlternatives = amountOfAlternatives;
    }
}
