package com.god.kahit.model;

public class Buff extends Modifier {
    private int amountOfalternatives;
    public Buff(String name, int price, int scoerMultiplier, int amountOfTime, int amountOfalternatives){
        super(price, name, scoerMultiplier, amountOfTime);
        this.amountOfalternatives = amountOfalternatives;

    }

    public int getAmountOfalternatives() {
        return amountOfalternatives;
    }
}
