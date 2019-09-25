package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;

public class QuestionClass extends AppCompatActivity {

    private static final String LOG_TAG = QuestionClass.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
    }

    public void launchScorePageClass (View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ScorePageClass.class);
        startActivity(intent);

    }

    public void launchBackChooseGameClass (View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameClass.class);
        startActivity(intent);

    }
}
