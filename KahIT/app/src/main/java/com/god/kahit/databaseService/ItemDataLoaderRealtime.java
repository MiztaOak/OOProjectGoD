package com.god.kahit.databaseService;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

/**
 * A helper class for the Firebase realtime database, that loads the item data from the database and
 * and stores it inside of a list of items and a map that pairs the names of items and the names of
 * their images
 *
 * @author Johan Ek
 */
public class ItemDataLoaderRealtime implements IItemDataLoader {
    private static Map<String, String> itemImageNameMap;
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
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

    public static Map<String, String> getItemImageNameMap() {
        return itemImageNameMap;
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
                    Item i = getItem(itemData);
                    buffList.add((Buff) i);
                }
                for (DataSnapshot itemData : debuffData.getChildren()) {
                    Item i = getItem(itemData);
                    debuffList.add((Debuff) i);
                }

                for (DataSnapshot itemData : vanityData.getChildren()) {
                    Item i = getItem(itemData);
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
    private Item getItem(DataSnapshot itemData) {
        int price = ((Long) Objects.requireNonNull(itemData.child("price").getValue())).intValue();

        String name = (String) itemData.child("name").getValue();
        String imgName = (String) itemData.child("img_name").getValue();

        itemImageNameMap.put(Objects.requireNonNull(name), Objects.requireNonNull(imgName));

        if (itemData.child("scoreMultiplier").exists()) { //If there is a scoreMulti then its a buff or debuff
            double scoreMultiplier;
            if (itemData.child("scoreMultiplier").getValue() instanceof Long) {
                scoreMultiplier = ((Long) Objects.requireNonNull(itemData.child("scoreMultiplier").getValue())).doubleValue();
            } else {
                scoreMultiplier = ((Double) Objects.requireNonNull(itemData.child("scoreMultiplier").getValue()));
            }
            int timeHeadstart = ((Long) Objects.requireNonNull(itemData.child("timeHeadstart").getValue())).intValue();
            if (itemData.child("autoAlt").exists()) { //If autoAlt exists then its a debuff
                boolean autorAlt = (Boolean) Objects.requireNonNull(itemData.child("autoAlt").getValue());
                return new Debuff(price, name, scoreMultiplier, timeHeadstart, autorAlt);
            } else {
                int amountOfAlternatives = ((Long) Objects.requireNonNull(itemData.child("amountOfAlternatives").getValue())).intValue();
                return new Buff(name, price, scoreMultiplier, timeHeadstart, amountOfAlternatives);
            }

        }
        return new VanityItem(price, name);
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
