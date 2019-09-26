package com.god.kahit.model;

public class StatAlterer extends BuyableItem {
    private int scoreMultiplier;
    private int timeMultiplier;
    private int timeHeadstart;
    private int amountOfAlternatives;


    public StatAlterer(int price, String type, String name, String imageSource, int scoreMultiplier, int timeMultiplier, int timeHeadstart, int amountOfAlternatives) {
        super(price, type, name, imageSource);
        this.scoreMultiplier = scoreMultiplier;
        this.timeMultiplier = timeMultiplier;
        this.timeHeadstart = timeHeadstart;
        this.amountOfAlternatives = amountOfAlternatives;
    }

    public int getScoreMultiplier() {
        return scoreMultiplier;
    }

    public void setScoreMultiplier(int scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    public int getTimeMultiplier() {
        return timeMultiplier;
    }

    public void setTimeMultiplier(int timeMultiplier) {
        this.timeMultiplier = timeMultiplier;
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
