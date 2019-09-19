package com.god.kahit.controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.AsyncPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;
import com.god.kahit.database.DatabaseHelper;

import java.io.IOException;

public class MainActivityClass extends AppCompatActivity {
    private static Context context;
    private static final String LOG_TAG = MainActivityClass.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    public void launchChooseGameClass(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameClass.class);
        startActivity(intent);
    }
}
