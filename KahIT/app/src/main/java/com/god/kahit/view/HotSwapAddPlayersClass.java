package com.god.kahit.view;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;

public class HotSwapAddPlayersClass extends AppCompatActivity {

    private static final String LOG_TAG = HotSwapAddPlayersClass.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotswap_add_players);
    }

    public void launchBackHotSwapGameModeClass(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, HotSwapGameModeClass.class);
        startActivity(intent);

    }

    public void launchQuestionClass(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionClass.class);
        startActivity(intent);

    }
}
