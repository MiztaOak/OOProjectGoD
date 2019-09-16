package com.god.kahit.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;

public class ChooseGameClass extends AppCompatActivity {

    private static final String LOG_TAG = ChooseGameClass.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_game_activity);
    }

    public void launchHostCreateRoomClass(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, HostCreateRoomClass.class);
        startActivity(intent);
    }

    public void launchBackMainActivityClass (View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityClass.class);
        startActivity(intent);

    }

    public void launchJoinRoomClass (View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, JoinRoomClass.class);
        startActivity(intent);

    }

    public void launchHotSwapGameModeClass(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, HotSwapGameModeClass.class);
        startActivity(intent);

    }
}
