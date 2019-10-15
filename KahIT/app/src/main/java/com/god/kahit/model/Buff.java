package com.god.kahit.model;
/**
 * @responsibility: This class is responsible for the buff items which have positive effect on
 * the player's stats.
 *
 * @used-by: This class is used in the following classes:
 * Player, QuizGame, Store and in the database.
 *
 * @author: Anas Alkoutli
 */

public class Buff extends Modifier {
    private int amountOfAlternatives;
    public Buff(String name, int price, int scoerMultiplier, int amountOfTime, int amountOfalternatives){
        super(price, name, scoerMultiplier, amountOfTime);
        this.amountOfAlternatives = amountOfalternatives;

    }

    public int getAmountOfAlternatives() {
        return amountOfAlternatives;
    }
}
