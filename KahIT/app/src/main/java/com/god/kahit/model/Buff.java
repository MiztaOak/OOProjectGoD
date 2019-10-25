package com.god.kahit.model;

/**
 * responsibility: This class is responsible for the buff items which have positive effect on
 * the player's stats.
 * used-by: This class is used in the following classes:
 * Player, QuizGame, ItemFactory, Store, ItemDataLoaderRealtime and IItemDataLoader.
 *
 * @author Anas Alkoutli
 */

public class Buff extends Modifier {
    private final int amountOfAlternatives;

    public Buff(String name, int price, double scoerMultiplier, int amountOfTime, int amountOfalternatives, String id) {
        super(price, name, scoerMultiplier, amountOfTime, id);
        this.amountOfAlternatives = amountOfalternatives;

    }

    /**
     * A method that returns the amount of alternatives to be taken away from a question
     *
     * @return : returns an int of how many alternatives to take away
     */
    int getAmountOfAlternatives() {
        return amountOfAlternatives;
    }
}
