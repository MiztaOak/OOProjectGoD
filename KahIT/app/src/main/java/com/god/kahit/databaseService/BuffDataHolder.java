package com.god.kahit.databaseService;

import com.god.kahit.model.Buff;
import com.god.kahit.model.Item;

/**
 * Class that holds the data for a buff that is fetched from the database, it also creates a Buff
 * based on it's data.
 *
 * used by: ItemDataLoaderRealtime
 *
 * @author Johan Ek
 */
public  class BuffDataHolder extends ModifierDataHolder{
    private long amountOfAlternatives;

    public BuffDataHolder() {
    }

    @Override
    public Item createItem() {
        return new Buff(getName(),getPrice(),getScoreMultiplier(),getTimeHeadstart(),(int)getAmountOfAlternatives(),getId());
    }

    public long getAmountOfAlternatives() {
        return amountOfAlternatives;
    }
}