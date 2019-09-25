package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;

public class ScorePageClass extends AppCompatActivity {


    private static final String LOG_TAG = ScorePageClass.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_activity);
    }

    public void launchBackQuestionClass (View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionClass.class);
        startActivity(intent);

    }

    public void launchChooseGameClass (View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameClass.class);
        startActivity(intent);

    }




}
