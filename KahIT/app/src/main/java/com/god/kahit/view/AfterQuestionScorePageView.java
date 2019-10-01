package com.god.kahit.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.god.kahit.R;
import com.god.kahit.ViewModel.AfterQuestionScorePageViewModel;

public class AfterQuestionScorePageView extends AppCompatActivity {
    private static final String LOG_TAG = AfterQuestionScorePageView.class.getSimpleName();
    private AfterQuestionScorePageViewModel model;
    private ObjectAnimator animator;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_question_score_page_activity);

        model = ViewModelProviders.of(this).get(AfterQuestionScorePageViewModel.class);

        final ProgressBar progressBar = findViewById(R.id.aqspProgressbar);

        setupRecycler();

        startTimer(progressBar);
    }

    private void setupRecycler(){
        recyclerView = findViewById(R.id.aqspScoreView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new ScorePageAdapter(model.getScoreScreenContents());
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void launchQuestionClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionClass.class);
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

                launchQuestionClass();
            }
        });
        animator.start();
    }
}
