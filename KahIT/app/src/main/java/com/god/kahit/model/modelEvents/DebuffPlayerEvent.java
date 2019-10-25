package com.god.kahit.model.modelEvents;

import com.god.kahit.model.Debuff;
import com.god.kahit.model.IEvent;

/**
 * Event used to notify a observer of the fact that a debuff should be applied to a player
 * <p>
 * used by: Store, Repository
 *
 * @author Johan Ek
 */
public class DebuffPlayerEvent implements IEvent {
    private final Debuff debuff;

    public DebuffPlayerEvent(Debuff debuff) {
        this.debuff = debuff;
    }

    public Debuff getDebuff() {
        return debuff;
    }
}
