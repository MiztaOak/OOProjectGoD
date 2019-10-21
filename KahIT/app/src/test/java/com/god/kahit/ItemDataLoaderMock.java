package com.god.kahit;

import com.god.kahit.model.Buff;
import com.god.kahit.model.Debuff;
import com.god.kahit.model.IItemDataLoader;
import com.god.kahit.model.VanityItem;

import java.util.ArrayList;
import java.util.List;

public class ItemDataLoaderMock implements IItemDataLoader {

    @Override
    public List<Buff> getBuffs() {
        List<Buff> buffs = new ArrayList<>();
        buffs.add(new Buff("Double Score",150,2.0,0,0,"i0"));
        buffs.add(new Buff("Time Headstart",120,1.0,5,0,"i1"));
        buffs.add(new Buff("Fifty Fifty",150,1.0,0,2,"i2"));
        return buffs;
    }

    @Override
    public List<Debuff> getDebuffs() {
        List<Debuff> debuffs = new ArrayList<>();
        debuffs.add(new Debuff(100,"Half Score",0.5,0,false,"i3"));
        debuffs.add(new Debuff(80,"Time is money",1,-5,false,"i4"));
        debuffs.add(new Debuff(80,"Let the robot do it",1,0,true,"i5"));
        return debuffs;
    }

    @Override
    public List<VanityItem> getVanityItems() {
        List<VanityItem> vanityItems = new ArrayList<>();
        vanityItems.add(new VanityItem(150,"Number One","i6"));
        vanityItems.add(new VanityItem(150,"Smart","i7"));
        vanityItems.add(new VanityItem(150,"Strong","i8"));
        return vanityItems;
    }
}
