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
    private ImageButton itemIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);
        storeText = findViewById(R.id.storeText);
        itemIcon = findViewById(R.id.itemIcon);
        int resId = getResources().getIdentifier(storeModel.getStoreItems().get(0).getImageSource() , "drawable", getPackageName());
        String text = "";
        for (int i = 0; i < storeModel.getStoreItems().size(); i++) {
            text += storeModel.getStoreItems().get(i).getName() + "\n";

        }
        System.out.println(resId + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        storeText.setText(text);
        itemIcon.setImageResource(resId);
    }

    public void launchStore(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, StoreControllerClass.class);
        startActivity(intent);
    }
}
