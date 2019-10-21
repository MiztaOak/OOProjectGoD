package com.god.kahit.model;

import java.util.List;

/**
 * Interface used to abstract the implementation of the persistent storage of items
 *
 * used by: ItemDataLoaderRealtime, ItemFactory
 *
 * @author Johan Ek
 */
public interface IItemDataLoader {
    List<Buff> getBuffs();
    List<Debuff> getDebuffs();
    List<VanityItem> getVanityItems();
}
