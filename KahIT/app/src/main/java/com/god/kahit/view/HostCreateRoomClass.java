package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;

import androidx.appcompat.app.AppCompatActivity;

public class HostCreateRoomClass extends AppCompatActivity {

    private static final String LOG_TAG = HostCreateRoomClass.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_create_room_activity);
    }

    public void launchBackChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchQuestionClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionClass.class);
        startActivity(intent);
    }

    public void launchTeamArrangementView(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, TeamArrangementView.class);
        startActivity(intent);
    }
}
