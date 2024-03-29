package com.god.kahit.model.modelEvents;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.Item;
import com.god.kahit.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Event used to notify a observer of the results of a lottery draw
 * <p>
 * used by: LotteryViewModel, QuizGame
 *
 * @author Mats Cedervall
 */
public class LotteryDrawEvent implements IEvent {
    private final Map<Player, Item> winnings;

    public LotteryDrawEvent(Map<Player, Item> winnings) {
        this.winnings = winnings;
    }

    public Map<Player, Item> getWinnings() {
        return new HashMap<>(winnings);
    }
}
