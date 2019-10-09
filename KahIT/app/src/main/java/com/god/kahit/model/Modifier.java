package com.god.kahit.model;

public class Modifier extends Item { //todo revise with better use of access-modifiers e.i not public everywhere
    private double scoreMultiplier;
    private int timeHeadstart;
    private int amountOfAlternatives;


    private boolean autoAnswer;
    //TODO add the last of the values like robot boolean

    public Modifier(int price, String type, String name, int scoreMultiplier, int timeHeadstart, int amountOfAlternatives){
        super(price, type, name);
        this.scoreMultiplier = scoreMultiplier;
        this.timeHeadstart = timeHeadstart;
        this.amountOfAlternatives = amountOfAlternatives;
    }

    public boolean isAutoAnswer() {
        return autoAnswer;
    }

    public void setAutoAnswer(boolean autoAnswer) {
        this.autoAnswer = autoAnswer;
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
