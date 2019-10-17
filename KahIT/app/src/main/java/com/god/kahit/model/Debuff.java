package com.god.kahit.model;
/**
 * @responsibility: This class is responsible for the debuff items which have negative effect on
 * the player's stats.
 *
 * @used-by: This class is used in the following classes:
 * Player, QuizGame, Store and in the database.
 *
 * @author: Anas Alkoutli
 */
public class Debuff extends Modifier {
    private Boolean autoAnswer;
    public Debuff(int price, String name, double scoreMultiplier, int timeHeadstart, boolean autoAnswer, String id){
        super(price, name, scoreMultiplier, timeHeadstart, id);
        this.autoAnswer = autoAnswer;
    }

    public Boolean getAutoAnswer() {
        return autoAnswer;
    }
}
