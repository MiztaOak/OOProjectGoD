package com.god.kahit.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.god.kahit.Events.AllPlayersReadyEvent;
import com.god.kahit.Events.GameLostConnectionEvent;
import com.god.kahit.Events.NewViewEvent;
import com.god.kahit.Events.PlayerAnsweredQuestionEvent;
import com.god.kahit.R;
import com.god.kahit.viewModel.QuestionViewModel;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.god.kahit.model.QuizGame.BUS;

public class QuestionView extends AppCompatActivity {
    private static final String LOG_TAG = QuestionView.class.getSimpleName();
    private final Handler h1 = new Handler();
    private QuestionViewModel model;
    private NavigationView navigationView;
    private TextView questionNmbTextView;
    private TextView playerNameTextView;
    private ImageView storeImage;
    private DrawerLayout drawerLayout;
    private Button choosePlayerButton;
    private ProgressBar progressBar;
    private ObjectAnimator animator;

    //TODO FOLLOWING is ALL TEMPORARY and will be replaced by variables from Question.class. FROM:
    private int questionDuration = 2000; //The total time the player1 has to answer.
    private int questionNmb = 1;  //The number of the question if in a sequence.
    private int totNmbQuestions = 3; //The total number of questions if in a sequence.

    private ArrayList<TextView> answers = new ArrayList<>();
    private boolean hasQuestionBeenShown;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        model = ViewModelProviders.of(this).get(QuestionViewModel.class);

        initLayoutViews();
        setupListeners(savedInstanceState);
        setupStore(savedInstanceState);

        model.nextQuestion();
        startTimer(progressBar, questionDuration);
    }

    private void initLayoutViews() {
        questionNmbTextView = findViewById(R.id.qNumOfQuesTextView);
        playerNameTextView = findViewById(R.id.qHotSwapCaseTextView);
        storeImage = findViewById(R.id.storeImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        choosePlayerButton = findViewById(R.id.choosePlayerButton);
        progressBar = findViewById(R.id.qProgressBar);

        final TextView answer1 = findViewById(R.id.qAnswer1TextView);
        final TextView answer2 = findViewById(R.id.qAnswer2TextView);
        final TextView answer3 = findViewById(R.id.qAnswer3TextView);
        final TextView answer4 = findViewById(R.id.qAnswer4TextView);

        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
    }

    private void setupListeners(final Bundle savedInstanceState) {
        choosePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initChoosePlayer(savedInstanceState);
            }
        });

        model.getQuestionText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                populateQuestionTextView(s);
            }
        });

        model.getQuestionAlts().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                populateAnswerTextViews(strings);
            }
        });

        model.getQuestionTime().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null) {
                    animator.setDuration(integer * 1000);
                    questionDuration = integer;
                }
            }
        });

        model.getPlayerName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                populatePlayerName(s);
            }
        });
    }

    private void setupStore(final Bundle savedInstanceState) {
        initStore(savedInstanceState);
        addDrawerListener();
        addStoreImageAction();
    }

    private void launchScorePageClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ScorePageView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void launchAfterQuestionScorePageClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, AfterQuestionScorePageView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        animator.pause();
        startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();
        hasQuestionBeenShown = false;
        updateQuestionNmbTextView(questionNmb++, totNmbQuestions); //todo implement real values
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
     * specifies what happens when an answer has been clicked.
     */
    public void OnAnswerClicked(View view) {
        model.onAnswerClicked(view, animator, answers);
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
    private void populateAnswerTextViews(List<String> answers) {
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
    private void populatePlayerName(String name) {
        playerNameTextView.setText(name);
    }

    /**
     * Sets the number of total questions.
     *
     * @param curNmbQuestion    equals to the current question index in a series of questions.
     * @param totalNmbQuestions equals to the total number of questions in a series of questions.
     */
    private void updateQuestionNmbTextView(int curNmbQuestion, int totalNmbQuestions) {
        questionNmbTextView.setText(String.format("Question %s of %s", curNmbQuestion, totalNmbQuestions));
    }

    /**
     * Starts a timer and visually presents it on the progressBar
     *
     * @param progressBar the ProgressBar the timer acts on.
     * @param timer       The total amount of time a question allows in seconds.
     */
    private void startTimer(ProgressBar progressBar, final int timer) {
        progressBar.setMax(10000);
        animator = ObjectAnimator.ofInt(progressBar, "progress", 0, progressBar.getMax());
        animator.setDuration(timer * 1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                if (model.isHotSwap()) {
                    final boolean isMoveOn = model.isMoveOn();
                    if (isMoveOn) {
                        model.updateViewForBeginningOfAnimation(answers);
                    }
                    h1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isMoveOn) {
                                launchAfterQuestionScorePageClass();
                            } else {
                                model.repeatQuestion();
                                model.resetColorOfTextView(answers);
                                animation.start();
                            }

                        }
                    }, 1000);
                } else if (answers.get(0).isEnabled()) {
                    model.greyOutAnswersTextView(answers);
                    model.sendIsReady();
                }
            }
        });
        animator.start();
    }

    /**
     * A method that initiates the store by getting its layout and pasting it inside the side
     * navigation
     */
    private void initStore(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StoreView.newInstance())
                    .commitNow();
        }
    }

    /**
     * A method that adds an action to the drawer layout which changes the position of storeImage
     * upon opening and closing
     */
    private void addDrawerListener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
            }

            @Override
            public void onDrawerOpened(View view) {
                navigationView.bringToFront();
                storeImage.setX(0);
            }

            @Override
            public void onDrawerClosed(View view) {
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

    /**
     * A method that adds action to the store image  which makes the store slides out when clicking
     * on it
     */
    private void addStoreImageAction() {
        storeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    /**
     * A method that initiates the a list to choose a player to debuff by getting its layout
     * and pasting it inside the side navigation
     */
    private void initChoosePlayer(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ChoosePlayerToDebuffView.newInstance())
                    .commitNow();
        }
    }

    @Subscribe
    public void onPlayerAnsweredQuestionEvent(PlayerAnsweredQuestionEvent event) {
        if (model.isMe(event.getPlayer())) {
            model.greyOutAnswersTextView(answers);
            model.colorSelectedAnswerTextView(answers);
            model.sendIsReady();
        }
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
        if (!hasQuestionBeenShown && !answers.get(0).isEnabled()) {
            animator.cancel();
            model.updateViewForBeginningOfAnimation(answers);
            model.resetPlayersReady();
            h1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    model.sendIsReady();
                }
            }, 1250);
            hasQuestionBeenShown = true;
        } else if (model.isHost() && hasQuestionBeenShown) {
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

