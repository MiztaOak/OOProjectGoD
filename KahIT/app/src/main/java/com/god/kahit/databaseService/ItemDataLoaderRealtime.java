package com.god.kahit.databaseService;

import android.content.Context;

import androidx.annotation.NonNull;

import com.god.kahit.model.Item;
import com.god.kahit.model.IItemDataLoader;
import com.god.kahit.model.Modifier;
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

/**
 * A helper class for the Firebase realtime database, that loads the item data from the database and
 * and stores it inside of a list of items and a map that pairs the names of items and the names of
 * their images
 *
 * @author Johan Ek
 */
public class ItemDataLoaderRealtime implements IItemDataLoader {
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;

    private static Map<String, String> itemImageNameMap;
    private List<Item> itemList;

    public ItemDataLoaderRealtime(Context context) {
        FirebaseApp.initializeApp(context);
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("items");

        if (itemImageNameMap == null) {
            itemImageNameMap = new HashMap<>();
        }
        itemList = new ArrayList<>();

        loadData();
    }

    /**
     * Method that attaches a ValueEventListener to the database reference
     */
    private void loadData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList = new ArrayList<>();
                for (DataSnapshot itemData : dataSnapshot.getChildren()) {
                    Item i = getItem(itemData);
                    itemList.add(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method that takes the data for one item from a DataSnapshot and converts it into an item
     * @param itemData the DataSnapshot pointing at the data for the item
     * @return the item that the data was converted into
     */
    private Item getItem(DataSnapshot itemData) {
        int price = ((Long) Objects.requireNonNull(itemData.child("price").getValue())).intValue();

        String name = (String) itemData.child("name").getValue();
        String imgName = (String) itemData.child("img_name").getValue();
        String type = (String) itemData.child("type").getValue();

        itemImageNameMap.put(Objects.requireNonNull(name), Objects.requireNonNull(imgName));

        if (itemData.child("scoreMultiplier").exists()) {
            int scoreMultiplier = ((Long) Objects.requireNonNull(itemData.child("scoreMultiplier").getValue())).intValue();
            int timeHeadstart = ((Long) Objects.requireNonNull(itemData.child("timeHeadstart").getValue())).intValue();
            int amountOfAlternatives = ((Long) Objects.requireNonNull(itemData.child("amountOfAlternatives").getValue())).intValue();
            return new Modifier(price, type, name, scoreMultiplier, timeHeadstart, amountOfAlternatives);
        }
        return new VanityItem(price, type, name);
    }
    
    public static Map<String, String> getItemImageNameMap() {
        return itemImageNameMap;
    }

    @Override
    public List<Item> getItems() {
        return itemList;
    }
}
