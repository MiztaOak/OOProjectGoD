package com.god.kahit.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;
import com.god.kahit.databaseService.DatabaseHelper;

public class MainActivityClass extends AppCompatActivity {
    private static final String LOG_TAG = MainActivityClass.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();
    }

    public void launchChooseGameClass(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameClass.class);
        startActivity(intent);
    }
}
