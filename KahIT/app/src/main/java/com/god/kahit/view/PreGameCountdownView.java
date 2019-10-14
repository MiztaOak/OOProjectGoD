package com.god.kahit.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.god.kahit.R;
import com.god.kahit.viewModel.PreGameCountdownViewModel;


/**
 * A class that shows a countdown timer before starting the game
 */
public class PreGameCountdownView extends AppCompatActivity {

    PreGameCountdownViewModel preGameCountdownTimer;
    TextView text;
    CountDownTimer counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_game_countdown_activity);

        preGameCountdownTimer = new PreGameCountdownViewModel(this);
        text = findViewById(R.id.cdTextView);

        preGameCountdownTimer.startToastMessage();
        startTimer();
        counter.start();
    }


    /**
     * Timer On-start and on-finish
     */
    private void startTimer() {
        counter = new CountDownTimer(4000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                text.setText(millisUntilFinished / 1000 + " " + "seconds remaining..");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                // text.setText("Time finished!");
                preGameCountdownTimer.finishToastMessage();

            }
        };
    }

}
