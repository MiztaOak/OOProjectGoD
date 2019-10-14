package com.god.kahit.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.viewModel.QuestionViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class QuestionView extends AppCompatActivity {

    private static final String LOG_TAG = QuestionView.class.getSimpleName();
    private final Handler h1 = new Handler();
    private ImageView storeImage;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    //TODO FOLLOWING is ALL TEMPORARY and will be replaced by variables from Question.class. FROM:
    int qTime = 2000; //The total time the player1 has to answer.
    int n = 5;  //The number of the question if in a sequence.
    int k = 11; //The total number of questions if in a sequence.
    String p1 = "The man with no name"; //The players name.
    private ObjectAnimator animation;
    private ArrayList<TextView> answers = new ArrayList<>();
    //TODO TO:

    private QuestionViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        model = ViewModelProviders.of(this).get(QuestionViewModel.class);
        storeImage = findViewById(R.id.storeImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        final Observer<String> questionTextObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                populateQuestionTextView(s);
            }
        };
        model.getQuestionText().observe(this, questionTextObserver);

        final Observer<List<String>> questionAltsObserver = new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                populateAnswerTextViews(strings);
            }
        };
        model.getQuestionAlts().observe(this, questionAltsObserver);

        final Observer<Integer> questionTimeObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null) {
                    animation.setDuration(integer * 1000);
                    qTime = integer;
                }
            }
        };
        model.getQuestionTime().observe(this, questionTimeObserver);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.qProgressBar);

        initStore(savedInstanceState);
        addDrawerListener();
        addStoreImageAction();
        initAnswerTextViews();
        populateQuestionNum(n);
        populateTotalNumQuestions(k);
        populatePlayerName(p1);
        model.nextQuestion();
        startTimer(progressBar, qTime);
    }

    /**
     * Sets up the answers list containing 4 TextViews where the answers are displayed.
     */
    public void initAnswerTextViews() {
        final TextView answer1 = findViewById(R.id.qAnswer1TextView);
        final TextView answer2 = findViewById(R.id.qAnswer2TextView);
        final TextView answer3 = findViewById(R.id.qAnswer3TextView);
        final TextView answer4 = findViewById(R.id.qAnswer4TextView);

        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
    }

    public void launchScorePageClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ScorePageView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchAfterQuestionScorePageClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, AfterQuestionScorePageView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchBackMainActivityClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        animation.pause();
        startActivity(intent);
    }

    public void onBackPressed() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        animation.pause();
        startActivity(intent);
    }

    @Override
    public void onPause(){
        super.onPause();
        animation.pause();
    }

    @Override
    public void onResume(){
        super.onResume();
        animation.resume();
    }

    /**
     * specifies what happens when an answer has been clicked.
     */
    public void OnAnswerClicked(View view) {
        model.onAnswerClicked(view, animation, answers);
    }

    /**
     * populates the textView where the question is displayed.
     *
     * @param question The question to be asked as a String.
     */
    public void populateQuestionTextView(String question) {
        TextView questionTextView = findViewById(R.id.qQuestionTextView);
        questionTextView.setText(question);
    }

    /**
     * populates the different textViews for all 4 different answers.
     *
     * @param answers is a List of Strings with alternative answers for the question.
     */
    public void populateAnswerTextViews(List<String> answers) {
        TextView[] qAnswersTextViews = {findViewById(R.id.qAnswer1TextView), findViewById(R.id.qAnswer2TextView), findViewById(R.id.qAnswer3TextView), findViewById(R.id.qAnswer4TextView)};
        for (int i = 0; i < qAnswersTextViews.length; i++) {
            qAnswersTextViews[i].setText(answers.get(i));
        }
    }

    /**
     * Sets the current player1 name in "hotswap/hot seat" mode.
     *
     * @param name
     */
    public void populatePlayerName(String name) {
        TextView playerName = findViewById(R.id.qHotSwapCaseTextView);
        playerName.setText(name);
    }

    /**
     * Sets the questions number.
     *
     * @param i equals to the number of the question asked, e.g. 4 out of 10 where 4 is i.
     */
    public void populateQuestionNum(int i) {
        TextView num = findViewById(R.id.qNumOfQuesTextView);
        num.setText(String.valueOf(i));
    }

    /**
     * Sets the number of total questions.
     *
     * @param i equals to the total number of questions in a series of questions.
     */
    public void populateTotalNumQuestions(int i) {
        TextView num = findViewById(R.id.textView9);
        num.setText(("of " + i));
    }

    /**
     * Starts a timer and visually presents it on the progressBar
     *
     * @param progressBar the ProgressBar the timer acts on.
     * @param timer       The total amount of time a question allows in seconds.
     */
    public void startTimer(ProgressBar progressBar, final int timer) {
        progressBar.setMax(10000);
        animation = ObjectAnimator.ofInt(progressBar, "progress", 0, progressBar.getMax());
        animation.setDuration(timer * 1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                model.updateViewForBeginningOfAnimation(animation, answers);
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        launchAfterQuestionScorePageClass();
                    }
                }, 1000);
            }
        });
        animation.start();

    }
    /**
     * A method that initiates the store by getting its layout and pasting it inside the side
     * navigation
     */
    public void initStore(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.storeContainer, StoreView.newInstance())
                    .commitNow();
        }
    }
    /**
     * A method that adds an action to the drawer layout which changes the position of storeImage
     * upon opening and closing
     */
    public void addDrawerListener(){
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
            }

            @Override
            public void onDrawerOpened( View view) {
                navigationView.bringToFront();
                storeImage.setX(0);
            }

            @Override
            public void onDrawerClosed( View view) {
                navigationView.bringToFront();
                storeImage.setX(Resources.getSystem().getDisplayMetrics().widthPixels - 190);
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        });
        storeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    public void addStoreImageAction(){
        storeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }
}

