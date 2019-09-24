package com.god.kahit.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Store;

public class StoreControllerClass extends AppCompatActivity {
    private static final String LOG_TAG = StoreControllerClass.class.getSimpleName();
    private Store storeModel= new Store();
    private TextView storeText;
    private ImageButton[] itemIcons = new ImageButton[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);
        storeText = findViewById(R.id.storeText);
        itemIcons[0]= findViewById(R.id.itemIcon);
        itemIcons[1]= findViewById(R.id.itemIcon2);
        itemIcons[2]= findViewById(R.id.itemIcon3);
        itemIcons[3]= findViewById(R.id.itemIcon4);
        String text = "";
        for (int i = 0; i < storeModel.getStoreItems().size(); i++) {
            text += storeModel.getStoreItems().get(i).getName() + "\n";
            int resId = getResources().getIdentifier(storeModel.getStoreItems().get(i).getImageSource() , "drawable", getPackageName());
            itemIcons[i].setImageResource(resId);
        }
        storeText.setText(text);
    }

    public void launchStore(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, StoreControllerClass.class);
        startActivity(intent);
    }
}
