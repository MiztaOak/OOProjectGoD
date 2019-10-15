package com.god.kahit.model;

public class Buff extends Modifier {
    private int amountOfAlternatives;
    public Buff(String name, int price, double scoerMultiplier, int amountOfTime, int amountOfalternatives){
        super(price, name, scoerMultiplier, amountOfTime);
        this.amountOfAlternatives = amountOfalternatives;

    }

    public int getAmountOfAlternatives() {
        return amountOfAlternatives;
    }
}
