package com.god.kahit.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Store;

public class StoreControllerClass extends AppCompatActivity {
    private static final String LOG_TAG = StoreControllerClass.class.getSimpleName();
    private Store storeModel= new Store();
    private TextView storeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);
        storeText = findViewById(R.id.storeText);
        storeText.setText(storeModel.getStoreItems().get(0).getName());
    }

    public void launchStore(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, StoreControllerClass.class);
        startActivity(intent);
    }
}
