package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;
import com.god.kahit.Repository;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityView extends AppCompatActivity {
    private static final String LOG_TAG = MainActivityView.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.main_activity);
        Repository.getInstance().startNewGameInstance(getApplicationContext());
    }

    public void launchChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
//        Intent intent = new Intent(this, ChooseGameView.class);
        Intent intent = new Intent(this, ChooseGameView.class);
        startActivity(intent);
    }
}
