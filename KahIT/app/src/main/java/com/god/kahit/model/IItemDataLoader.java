package com.god.kahit.model;

import java.util.List;

/**
 * Interface used to abstract the implementation of the persistent storage of items
 *
 * @author Johan Ek
 */
public interface IItemDataLoader {
    List<Buff> getBuffs();

    List<Debuff> getDebuffs();

    List<VanityItem> getVanityItems();
}
