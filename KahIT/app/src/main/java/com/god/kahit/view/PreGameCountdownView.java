package com.god.kahit.view;

import android.content.Intent;
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
    private static final long COUNTDOWN_DURATION = 5000;
    private static final long ONE_SECOND = 1000;
    private PreGameCountdownViewModel preGameCountdownTimer;
    private TextView text;
    private CountDownTimer counter;

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
        counter = new CountDownTimer(COUNTDOWN_DURATION, ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                text.setText(String.format("%s seconds remaining..", millisUntilFinished / ONE_SECOND));
            }

            @Override
            public void onFinish() {
                preGameCountdownTimer.finishToastMessage();
                Intent intent = new Intent(getApplicationContext(), QuestionView.class);
                startActivity(intent);
            }
        };
    }

}
