package com.god.kahit.databaseService;

import com.god.kahit.model.Debuff;
import com.god.kahit.model.Item;

/**
 * Class that holds the data for a debuff that is fetched from the database, it also creates a Debuff
 * based on it's data.
 *
 * used by: ItemDataLoaderRealtime
 *
 * @author Johan Ek
 */
public class DebuffDataHolder extends ModifierDataHolder {
    private boolean autoAlt;

    public DebuffDataHolder() {
    }

    @Override
    public Item createItem() {
        return new Debuff(getPrice(),getName(),getScoreMultiplier(),getTimeHeadstart(),autoAlt,getId());
    }

    public boolean isAutoAlt() {
        return autoAlt;
    }
}
