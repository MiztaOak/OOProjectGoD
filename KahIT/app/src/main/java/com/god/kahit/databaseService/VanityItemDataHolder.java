package com.god.kahit.databaseService;

import com.god.kahit.model.Item;
import com.god.kahit.model.VanityItem;

/**
 * Class that holds the data for a vanityItem that is fetched from the database, it also creates a
 * vanityItem based on it's data.
 *
 * used by: ItemDataLoaderRealtime
 *
 * @author Johan Ek
 */
public class VanityItemDataHolder extends ItemDataHolder{
    public VanityItemDataHolder() {
        super();
    }

    @Override
    public Item createItem() {
        return new VanityItem(getPrice(),getName(),getId());
    }
}
