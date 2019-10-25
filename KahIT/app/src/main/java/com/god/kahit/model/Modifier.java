package com.god.kahit.model;

/**
 * responsibility: This is an abstract class used to group up the shared values
 * between Buffs and Debuffs.
 * used-by: This class is used in the following classes:
 * Buffs and Debuffs.
 * @author: Anas Alkoutli
 */
abstract class Modifier extends Item {
    private double scoreMultiplier;
    private int amountOfTime;

    Modifier(int price, String name, double scoreMultiplier, int amountOfTime, String id) {
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
