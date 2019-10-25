package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * responsibility: ViewModel for the LobbyNetView
 * <p>
 * used-by: LobbyNetView.
 *
 * @author Mats Cedervall
 */
public class ScorePageView extends AppCompatActivity {

    private static final String LOG_TAG = ScorePageView.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_activity);
    }

    public void launchBackMainActivityClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onBackPressed() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
