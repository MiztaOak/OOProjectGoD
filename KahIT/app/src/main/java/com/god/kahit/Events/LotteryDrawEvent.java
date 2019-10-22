package com.god.kahit.Events;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.Item;
import com.god.kahit.model.Player;

import java.util.HashMap;
import java.util.Map;

public class LotteryDrawEvent implements IEvent {
    private Map<Player, Item> winnings;

    public LotteryDrawEvent(Map<Player, Item> winnings) {
        this.winnings = winnings;
    }

    public Map<Player, Item> getWinnings() {
        return new HashMap<>(winnings);
    }
}
