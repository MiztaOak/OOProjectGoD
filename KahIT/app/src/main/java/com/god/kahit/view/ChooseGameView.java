package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseGameView extends AppCompatActivity {

    private static final String LOG_TAG = ChooseGameView.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_game_activity);
    }

    public void launchBackMainActivityClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchHostCreateRoomClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, CreateLobbyNetView.class);
        startActivity(intent);
    }

    public void launchJoinRoomClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, JoinLobbyNetView.class);
        startActivity(intent);
    }

    public void launchHotSwapGameModeClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, HotSwapGameModeView.class);
        startActivity(intent);
    }

    public void launchSidenavTest(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, StoreView.class);
        startActivity(intent);
    }

    public void launchLotteryTest(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, LotteryView.class);
        startActivity(intent);
    }
}
