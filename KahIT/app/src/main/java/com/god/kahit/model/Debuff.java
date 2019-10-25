package com.god.kahit.model;

/**
 * @esponsibility: This class is responsible for the debuff items which have negative effect on
 * the player's stats.
 * used-by: This class is used in the following classes:
 * Player, QuizGame, ItemFactory, Store, ItemDataLoaderRealtime and IItemDataLoader.
 * @author: Anas Alkoutli
 */
public class Debuff extends Modifier {
    private Boolean autoAnswer;

    public Debuff(int price, String name, double scoreMultiplier, int timeHeadstart, boolean autoAnswer, String id) {
        super(price, name, scoreMultiplier, timeHeadstart, id);
        this.autoAnswer = autoAnswer;
    }

    /**
     * A method that returns if tha game will answer instead of a player.
     *
     * @return : boolean that indicates of the game should answer or not
     */
    Boolean getAutoAnswer() {
        return autoAnswer;
    }
}
