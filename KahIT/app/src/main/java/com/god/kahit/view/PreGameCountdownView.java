package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.god.kahit.R;
import com.god.kahit.applicationEvents.AllPlayersReadyEvent;
import com.god.kahit.applicationEvents.GameLostConnectionEvent;
import com.god.kahit.applicationEvents.NewViewEvent;
import com.god.kahit.viewModel.PreGameCountdownViewModel;

import org.greenrobot.eventbus.Subscribe;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * responsibility: A class that shows a countdown timer before starting the game.
 * used-by: LobbyNetView.
 *
 * @author Oussama Anadani, Mats Cedervall
 */
public class PreGameCountdownView extends AppCompatActivity {
    private static final String LOG_TAG = LobbyNetView.class.getSimpleName();
    private static final long COUNTDOWN_DURATION = 5000; // how many ticks in milliseconds
    private static final long ONE_SECOND = 1000; // duration between ticks in milliseconds
    private PreGameCountdownViewModel preGameCountdownViewModel;
    private TextView sessionTypeTextView;
    private TextView countdownTextView;
    private CountDownTimer counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pre_game_countdown_activity);
        defineInstanceVariables();
        preGameCountdownViewModel.startToastMessage();
        startTimer();
        counter.start();
    }

    private void defineInstanceVariables() {
        preGameCountdownViewModel = ViewModelProviders.of(this).get(PreGameCountdownViewModel.class);
        preGameCountdownViewModel.setContext(getApplicationContext());
        sessionTypeTextView = findViewById(R.id.cd_SessionType_textView);
        countdownTextView = findViewById(R.id.cdTextView);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (preGameCountdownViewModel.isHotSwap()) {
            sessionTypeTextView.setText("Hotswap mode");
        } else {
            sessionTypeTextView.setText(String.format("%s - id: '%s'", preGameCountdownViewModel.isHost() ? "Host" : "Client", preGameCountdownViewModel.getMyPlayerId()));
        }

        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BUS.unregister(this);
    }

    /**
     * A method that defines a timer who has On-Tick and on-finish methods.
     * The timer runs by given ticks in milliseconds and duration between ticks in milliseconds.
     */
    private void startTimer() {
        counter = new CountDownTimer(COUNTDOWN_DURATION, ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.format("%s seconds remaining..", millisUntilFinished / ONE_SECOND));
            }

            @Override
            public void onFinish() {
                preGameCountdownViewModel.finishToastMessage();
                preGameCountdownViewModel.sendIsReady();
                countdownTextView.setText("Waiting for server..");
            }
        };
    }

    @Subscribe
    public void onNewViewEvent(NewViewEvent event) {
        preGameCountdownViewModel.resetPlayersReady();
        Intent intent = new Intent(getApplicationContext(), event.getNewViewClass());
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onAllPlayersReadyEvent(AllPlayersReadyEvent event) {
        countdownTextView.setText("All players ready!");
        if (preGameCountdownViewModel.isHost()) {
            preGameCountdownViewModel.showNextView();
        }
    }

    @Subscribe
    public void onGameLostConnectionEvent(GameLostConnectionEvent event) {
        if (!preGameCountdownViewModel.isHost()) {
            Log.d(LOG_TAG, "onGameLostConnectionEvent: event triggered");

            Toast.makeText(getApplicationContext(), "Lost connection to game!",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ChooseGameView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Log.d(LOG_TAG, "onGameLostConnectionEvent: event triggered, but I am host - skipping");
        }
    }
}
