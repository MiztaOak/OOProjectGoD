package com.god.kahit.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.god.kahit.R;
import com.god.kahit.applicationEvents.AllPlayersReadyEvent;
import com.god.kahit.applicationEvents.GameLostConnectionEvent;
import com.god.kahit.applicationEvents.NewViewEvent;

import org.greenrobot.eventbus.Subscribe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * The view class for AfterQuestionScorePage, displaying a leaderboard
 * <p>
 * used by: CategoryView, QuestionViewModel, Repository
 *
 * @author Johan Ek &
 */
public class AfterQuestionScorePageView extends AppCompatActivity {
    private static final String LOG_TAG = AfterQuestionScorePageView.class.getSimpleName();
    private com.god.kahit.viewModel.AfterQuestionScorePageViewModel model;
    private ObjectAnimator animator;

    private TextView sessionTypeTextView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_question_score_page_activity);

        model = ViewModelProviders.of(this).get(com.god.kahit.viewModel.AfterQuestionScorePageViewModel.class);
        sessionTypeTextView = findViewById(R.id.aqsp_SessionType_textView);
        final ProgressBar progressBar = findViewById(R.id.aqspProgressbar);

        setupRecycler();
        startTimer(progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (model.isHotSwap()) {
            sessionTypeTextView.setText("Hotswap mode");
        } else {
            sessionTypeTextView.setText(String.format("%s - id: '%s'", model.isHost() ? "Host" : "Client", model.getMyPlayerId()));
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

    @Override
    public void onPause() {
        super.onPause();
        animator.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        animator.resume();
    }

    private void setupRecycler() {
        recyclerView = findViewById(R.id.aqspScoreRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new ScorePageAdapter(model.getScoreScreenContents(), model.getMyPlayerId(), model.isHotSwap());
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void launchBackMainActivityClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        animator.pause();
        startActivity(intent);
    }

    public void onBackPressed() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        animator.pause();
        startActivity(intent);
    }

    public void launchQuestionClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchLotteryView() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, LotteryView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void startTimer(ProgressBar progressBar) {
        progressBar.setMax(10000);
        animator = ObjectAnimator.ofInt(progressBar, "progress", 0, progressBar.getMax());
        animator.setDuration(5000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (model.isHotSwap()) {
                    if (model.isRoundOver()) {
                        launchLotteryView();
                    } else {
                        launchQuestionClass();
                    }
                } else {
                    model.sendIsReady();
                }
            }
        });
        animator.start();
    }

    @Subscribe
    public void onNewViewEvent(NewViewEvent event) {
        model.resetPlayersReady();
        Intent intent = new Intent(getApplicationContext(), event.getNewViewClass());
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onAllPlayersReadyEvent(AllPlayersReadyEvent event) {
//        countdownTextView.setText("All players ready!"); //todo show waiting for server etc
        if (model.isHost()) {
            Log.d(LOG_TAG, "onAllPlayersReadyEvent: event triggered, showing next view.");
            model.showNextView();
        }
    }

    @Subscribe
    public void onGameLostConnectionEvent(GameLostConnectionEvent event) {
        if (!model.isHost()) {
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
