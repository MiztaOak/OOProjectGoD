package com.god.kahit.view;

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
    private TextView storeText, pointsText;
    private ImageButton[] itemIcons = new ImageButton[4]; //Array of imageButton
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);

    }
}
