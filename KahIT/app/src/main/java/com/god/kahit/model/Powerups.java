package com.god.kahit.model;

public class Powerups extends BuyableItems {
    private int scoreMultiplier;
    private int timeMultiplier;
    private int timeHeadstart;
    private int amountOfAlternatives;
    private User choosenUser;

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

    public User getChoosenUser() {
        return choosenUser;
    }

    public void setChoosenUser(User choosenUser) {
        this.choosenUser = choosenUser;
    }
}
