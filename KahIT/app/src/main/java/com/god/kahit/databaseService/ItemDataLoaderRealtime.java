package com.god.kahit.databaseService;

import android.content.Context;
import android.util.Log;

import com.god.kahit.model.Buff;
import com.god.kahit.model.Debuff;
import com.god.kahit.model.IItemDataLoader;
import com.god.kahit.model.Item;
import com.god.kahit.model.VanityItem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

/**
 * A helper class for the Firebase realtime database, that loads the item data from the database and
 * and stores it inside of a list of items and a map that pairs the names of items and the names of
 * their images
 * <p>
 * used by: LotteryView, StoreView, Repository
 *
 * @author Johan Ek
 */
public class ItemDataLoaderRealtime implements IItemDataLoader {
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    private static Map<String, String> itemImageNameMap;

    private List<Buff> buffList;
    private List<Debuff> debuffList;
    private List<VanityItem> vanityItemList;

    public ItemDataLoaderRealtime(Context context) {
        FirebaseApp.initializeApp(context);
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("items");

        if (itemImageNameMap == null) {
            itemImageNameMap = new HashMap<>();
        }
        buffList = new ArrayList<>();
        debuffList = new ArrayList<>();
        vanityItemList = new ArrayList<>();

        loadData();
    }

    /**
     * Method that attaches a ValueEventListener to the database reference
     */
    private void loadData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                buffList = new ArrayList<>();
                debuffList = new ArrayList<>();
                vanityItemList = new ArrayList<>();

                DataSnapshot buffData = dataSnapshot.child("buffs");
                DataSnapshot debuffData = dataSnapshot.child("debuffs");
                DataSnapshot vanityData = dataSnapshot.child("vanityItems");

                for (DataSnapshot itemData : buffData.getChildren()) {
                    Item i = getItem(itemData, "buffs");
                    buffList.add((Buff) i);
                }
                for (DataSnapshot itemData : debuffData.getChildren()) {
                    Item i = getItem(itemData, "debuffs");
                    debuffList.add((Debuff) i);
                }

                for (DataSnapshot itemData : vanityData.getChildren()) {
                    Item i = getItem(itemData, "vanityItems");
                    vanityItemList.add((VanityItem) i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value", databaseError.toException());
            }
        });
    }

    /**
     * Method that takes the data for one item from a DataSnapshot and converts it into an item
     *
     * @param itemData the DataSnapshot pointing at the data for the item
     * @return the item that the data was converted into
     */
    private Item getItem(DataSnapshot itemData, String key) {
        ItemDataHolder itemDataHolder = null;
        switch (key) {
            case "buffs":
                itemDataHolder = itemData.getValue(BuffDataHolder.class);
                break;
            case "debuffs":
                itemDataHolder = itemData.getValue(DebuffDataHolder.class);
                break;
            case "vanityItems":
                itemDataHolder = itemData.getValue(VanityItemDataHolder.class);
                break;
        }
        if (itemDataHolder != null) {
            itemImageNameMap.put(itemDataHolder.getName(),itemDataHolder.getImg_name());
            return itemDataHolder.createItem();
        }
        return null;
    }


    public static Map<String, String> getItemImageNameMap() {
        return itemImageNameMap;
    }

    @Override
    public List<Buff> getBuffs() {
        if (buffList == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return buffList;
    }

    @Override
    public List<Debuff> getDebuffs() {
        if (debuffList == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return debuffList;
    }

    @Override
    public List<VanityItem> getVanityItems() {
        if (vanityItemList == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return vanityItemList;
    }
}
