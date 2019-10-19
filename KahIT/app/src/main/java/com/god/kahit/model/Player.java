package com.god.kahit.model;

public class Player { //todo revise with better use of access-modifiers. e.i not public everywhere
    private String id;
    private String name;
    private int score;
    private double scoreMultiplier = 1;
    private int amountOfTime;
    private int amountOfAlternatives;
    private boolean autoAnswer;
    private VanityItem vanityItem;
    private boolean isReady; //TODO check if this really is needed in the model since it should prob be in lobby


    public Player(String name, String id) {
        this.name = name;
        this.id = id;
        this.score = 500; //TODO remove when done testing
        this.isReady = false;
    }



    /**
     * A method that sets the values of effects of a Buff to players own values
     */
    public void setBuff(Buff buff) {
        scoreMultiplier = buff.getScoreMultiplier() *scoreMultiplier;
        amountOfTime = buff.getAmountOfTime() + amountOfTime;
        this.amountOfAlternatives = buff.getAmountOfAlternatives();
    }
    /**
     * A method that sets the values of effects of a Debuff to players own values
     */
    public void setDebuff(Debuff debuff) {
        scoreMultiplier = debuff.getScoreMultiplier() *scoreMultiplier;
        amountOfTime = debuff.getAmountOfTime() + amountOfTime;
        autoAnswer= debuff.getAutoAnswer();
    }
    //TODO remove when done implemnting buffs and debuffs.
    public void setModifier(Modifier modifier){}
    /**
     * A method that clears the effect of a Modifier after  it has been used
     */
    public void clearModifier(){
        this.scoreMultiplier = 1;
        this.amountOfTime = 0;
        this.amountOfAlternatives = 0;
        this.autoAnswer = false;
    }

    public void updateScore(int points) { //todo rename to addScore
        this.score += points*scoreMultiplier;
    } //TODO add calculation that takes current buff into account

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public void setScoreMultiplier(double scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    public int getAmountOfTime() {
        return amountOfTime;
    }

    public void setAmountOfTime(int amountOfTime) {
        this.amountOfTime = amountOfTime;
    }

    public int getAmountOfAlternatives() {
        return amountOfAlternatives;
    }

    public void setAmountOfAlternatives(int amountOfAlternatives) {
        this.amountOfAlternatives = amountOfAlternatives;
    }

    public boolean isAutoAnswer() {
        return autoAnswer;
    }

    public void setAutoAnswer(boolean autoAnswer) {
        this.autoAnswer = autoAnswer;
    }

    public VanityItem getVanityItem() {
        return vanityItem;
    }

    public void setVanityItem(VanityItem vanityItem) {
        this.vanityItem = vanityItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


