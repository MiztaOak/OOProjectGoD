package com.god.kahit.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.god.kahit.R;
import com.god.kahit.applicationEvents.AllPlayersReadyEvent;
import com.god.kahit.applicationEvents.GameLostConnectionEvent;
import com.god.kahit.applicationEvents.NewViewEvent;
import com.god.kahit.applicationEvents.PlayerAnsweredQuestionEvent;
import com.god.kahit.viewModel.QuestionViewModel;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * The view class for the view where the user can answer questions and by items
 * <p>
 * used by: AfterQuestionScorePageView, AfterQuestionScorePageViewModel, CategoryView,
 * CategoryViewModel, ChooseGameView, HotSwapAddPlayerView, PreGameCountdownViewModel,
 * Repository
 *
 * @author Johan Ek & Jakob Ewerstrand
 */
public class QuestionView extends AppCompatActivity {
    private static final String LOG_TAG = QuestionView.class.getSimpleName();
    private final Handler h1 = new Handler();
    private QuestionViewModel model;
    private NavigationView navigationView;
    private TextView sessionTypeTextView;
    private TextView questionNmbTextView;
    private TextView playerNameTextView;
    private ImageView storeImage;
    private DrawerLayout drawerLayout;
    private ProgressBar progressBar;
    private ObjectAnimator animator;

    private int questionDuration = 2000; //The total time the player has to answer.
    private int questionNmb = 1;  //The number of the question if in a sequence.

    private ArrayList<TextView> answers = new ArrayList<>();
    private boolean hasQuestionBeenShown;
    private int indexOfClickedView = -1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        model = ViewModelProviders.of(this).get(QuestionViewModel.class);
        getLifecycle().addObserver(model);

        initLayoutViews();
        setupListeners();
        setupStore(savedInstanceState);

        model.nextQuestion();
        startTimer(progressBar, questionDuration);

        model.startCategoryPlaylist(this);
    }

    /**
     * A method that initiates the view. It finds the necessary views
     * and calls the methods needed for the view to function
     */
    private void initLayoutViews() {
        sessionTypeTextView = findViewById(R.id.q_SessionType_textView);
        questionNmbTextView = findViewById(R.id.qNumOfQuesTextView);
        playerNameTextView = findViewById(R.id.qHotSwapCaseTextView);
        storeImage = findViewById(R.id.storeImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        progressBar = findViewById(R.id.qProgressBar);

        final TextView answer1 = findViewById(R.id.qAnswer1TextView);
        final TextView answer2 = findViewById(R.id.qAnswer2TextView);
        final TextView answer3 = findViewById(R.id.qAnswer3TextView);
        final TextView answer4 = findViewById(R.id.qAnswer4TextView);

        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
        addActionToAnswers();
    }

    /**
     * A method that sets up listeners to the question value that are in the model.
     */
    private void setupListeners() {
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

    /**
     * A method that initiates the store and calls methods that are needed for the store.
     *
     * @param savedInstanceState: Bundle
     */
    private void setupStore(final Bundle savedInstanceState) {
        initStore(savedInstanceState);
        addDrawerListener();
        addStoreImageAction();
    }

    /**
     * A method that launches the question score page
     */
    private void launchAfterQuestionScorePageClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, AfterQuestionScorePageView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        animator.pause();
        startActivity(intent);
    }

    /**
     * A method that launches the main activity. Acts as a back button.
     *
     * @param view: The view that is clicked.
     */
    public void launchBackMainActivityClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        animator.pause();
        startActivity(intent);
    }

    /**
     * A method that launches main activity.
     */
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

        if (model.isHotSwap()) {
            sessionTypeTextView.setText("Hotswap mode");
        } else {
            sessionTypeTextView.setText(String.format("%s - id: '%s'", model.isHost() ? "Host" : "Client", model.getMyPlayerId()));
        }

        //The total number of questions if in a sequence.
        int totNmbQuestions = 3;
        updateQuestionNmbTextView(questionNmb++, totNmbQuestions);
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
            public void onDrawerSlide(@NonNull View view, float v) {
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                navigationView.bringToFront();
                storeImage.setX(0);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
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
     * A method that adds action to the store image which makes the store slides out when clicking
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
     * sets a new background color for the non-selected answers.
     */
    private void greyOutNonSelectedAnswers() {
        if (indexOfClickedView < 0) {
            for (int i = 0; i < answers.size(); i++) {
                answers.get(i).setEnabled(false);
                Drawable answerDrawable = answers.get(i).getBackground();
                answerDrawable = DrawableCompat.wrap(answerDrawable);
                DrawableCompat.setTint(answerDrawable, ContextCompat.getColor(this, R.color.light_grey));

                answers.get(i).setBackground(answerDrawable);
            }
        } else {
            for (int i = 0; i < answers.size(); i++) {
                answers.get(i).setEnabled(false);
                if (!(answers.get(i) == (answers.get(indexOfClickedView)))) {
                    Drawable answerDrawable = answers.get(i).getBackground();
                    answerDrawable = DrawableCompat.wrap(answerDrawable);
                    DrawableCompat.setTint(answerDrawable, ContextCompat.getColor(this, R.color.light_grey));

                    answers.get(i).setBackground(answerDrawable);
                }
            }
        }
    }

    /**
     * Updates the view after the animation has finished. Showing a green color if the answer was right, red if not.
     * If no action has been taken all answers are grayed out.
     */
    private void updateViewAfterAnimation() {
        greyOutNonSelectedAnswers();
        if (indexOfClickedView >= 0) {
            Drawable answerDrawable = answers.get(indexOfClickedView).getBackground();
            answerDrawable = DrawableCompat.wrap(answerDrawable);

            if (model.isCorrectAnswer()) {
                DrawableCompat.setTint(answerDrawable, ContextCompat.getColor(this, R.color.green));
                answers.get(indexOfClickedView).setBackground(answerDrawable);
            } else {
                DrawableCompat.setTint(answerDrawable, ContextCompat.getColor(this, R.color.red));
                answers.get(indexOfClickedView).setBackground(answerDrawable);
            }
        }
    }

    /**
     * Resets the color of the textViews.
     */
    private void resetAnswerTextViews() {
        Drawable answerDrawable;
        List<Integer> colors = getAnswerColors();

        Collections.shuffle(colors);
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setEnabled(true);

            answerDrawable = answers.get(i).getBackground();
            answerDrawable = DrawableCompat.wrap(answerDrawable);
            DrawableCompat.setTint(answerDrawable, colors.get(i));

            answers.get(i).setBackground(answerDrawable);
        }
    }

    /**
     * Gets the color id used for the answer TextViews
     *
     * @return The List of id for colors.
     */
    private List<Integer> getAnswerColors() {
        List<Integer> answerColors = new ArrayList<>();
        int[] retrieve = this.getResources().getIntArray(R.array.answerColors);
        for (int re : retrieve) {
            answerColors.add(re);
        }
        return answerColors;
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
     * This method calls for a method that checks for buffs and debuffs too.
     *
     * @param answers is a List of Strings with alternative answers for the question.
     */
    private void populateAnswerTextViews(List<String> answers) {
        TextView[] qAnswersTextViews = {findViewById(R.id.qAnswer1TextView), findViewById(R.id.qAnswer2TextView), findViewById(R.id.qAnswer3TextView), findViewById(R.id.qAnswer4TextView)};
        for (int i = 0; i < qAnswersTextViews.length; i++) {
            qAnswersTextViews[i].setText(answers.get(i));
        }
        checkForEffects();
    }

    /**
     * Sets the playerNameTextView text into the current player's name
     *
     * @param name is a String containing the current player's name
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
                    updateViewAfterAnimation();
                    h1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isMoveOn) {
                                model.incrementCurrentPlayer();
                                launchAfterQuestionScorePageClass();
                            } else {
                                model.repeatQuestion();
                                resetAnswerTextViews();
                                animation.start();
                            }

                        }
                    }, 1000);
                } else if (answers.get(0).isEnabled()) {
                    greyOutNonSelectedAnswers();
                    model.sendIsReady();
                }
            }
        });
        animator.start();
    }

    @Subscribe
    public void onPlayerAnsweredQuestionEvent(PlayerAnsweredQuestionEvent event) {
        if (model.isMe(event.getPlayer())) {
            greyOutNonSelectedAnswers();

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
        if (!hasQuestionBeenShown && !answers.get(0).isEnabled()) {
            animator.cancel();
            updateViewAfterAnimation();
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

    /**
     * A method that checks for buffs and debuffs to run their visual effects.
     */
    private void checkForEffects() {
        if (model.isHalfTheAlternatives()) {
            halfTheAlternativesEffect(answers.size());
        } else if (model.isAutoAnswer()) {
            runAutoAnswer();
        }
    }

    /**
     * A method that sets an action to the text views that hold alternatives for the question.
     * Calls for the answer method in the model view.
     */
    private void addActionToAnswers() {
        for (final TextView answer : answers) {
            answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView = (TextView) view;
                    indexOfClickedView = answers.indexOf(textView);
                    if (model.isHotSwap() || model.isHost()) {
                        greyOutNonSelectedAnswers();
                    }
                    model.onAnswerClicked(indexOfClickedView, animator);
                }
            });
        }
    }

    /**
     * A method that starts the effect of the Fifty fifty buff,
     * which is to hide two answers out of 4.
     * This method should not hide the right alternative.
     *
     * @param size: The size of the list of strings which are the alternatives
     */
    private void halfTheAlternativesEffect(int size) {
        answers.get(model.getTwoIndexes(size).first).setVisibility(View.INVISIBLE);
        answers.get(model.getTwoIndexes(size).second).setVisibility(View.INVISIBLE);
    }

    /**
     * A method that runs the autoAnswer debuff effect.
     */
    public void runAutoAnswer() {
        answers.get(model.autoChooseAnswer()).performClick();

    }
}

