package com.god.kahit.model;

public class Debuff extends Modifier {
    private Boolean autoAnswer;
    public Debuff(int price, String name, int scoreMultiplier, int timeHeadstart, boolean autoAnswer){
        super(price, name, scoreMultiplier, timeHeadstart);
        this.autoAnswer = autoAnswer;
    }

    public Boolean getAutoAnswer() {
        return autoAnswer;
    }
}
