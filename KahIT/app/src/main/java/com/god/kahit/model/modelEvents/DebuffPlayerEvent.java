package com.god.kahit.model.modelEvents;

import com.god.kahit.model.Debuff;
import com.god.kahit.model.IEvent;

/** Event used to notify a observer of the fact that a debuff should be applied to a player
 *
 * used by: QuestionViewModel, QuizGame
 *
 * @author Johan Ek
 */
public class DebuffPlayerEvent implements IEvent {
    private Debuff debuff;

    public DebuffPlayerEvent(Debuff debuff) {
        this.debuff = debuff;
    }

    public Debuff getDebuff() {
        return debuff;
    }
}
