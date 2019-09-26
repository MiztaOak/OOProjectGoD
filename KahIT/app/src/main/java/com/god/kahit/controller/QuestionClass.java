package com.god.kahit.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.god.kahit.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionClass extends AppCompatActivity {

    private static final String LOG_TAG = QuestionClass.class.getSimpleName();
    private ObjectAnimator animation;
    private long timeLeft;
    private ArrayList<TextView> answers = new ArrayList<>();
    private final Handler h1 = new Handler();

    //TODO FOLLOWING is ALL TEMPORARY and will be replaced by variables from Question.class. FROM:
    int eTime = 20; //The total time the player1 has to answer.
    int n = 5;  //The number of the question if in a sequence.
    int k = 11; //The total number of questions if in a sequence.
    String q1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit?"; //This is the question to be asked.
    String p1 = "The man with no name"; //The players name.
    List<String> an = Arrays.asList("HELLO", "WORLD", "LOVE", "ZORAN"); //Answers as a list.
    //TODO TO:


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        final ProgressBar progressBar =(ProgressBar)findViewById(R.id.qProgressBar);

        initAnswerTextViews();
        populateQuestionNum(n);
        populateTotalNumQuestions(k);
        populateQuestionTextView(q1);
        populatePlayerName(p1);
        populateAnswerTextViews(an);
        startTimer(progressBar, eTime);
    }

    /**
     * Sets up the answers list containing 4 TextViews where the answers are displayed.
     */
    public void initAnswerTextViews() {
        final TextView answer1 = (TextView) findViewById(R.id.qAnswer1TextView);
        final TextView answer2 = (TextView) findViewById(R.id.qAnswer2TextView);
        final TextView answer3 = (TextView) findViewById(R.id.qAnswer3TextView);
        final TextView answer4 = (TextView) findViewById(R.id.qAnswer4TextView);

        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
    }

    public void launchScorePageClass (){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ScorePageClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchBackMainActivityClass(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onBackPressed(){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, MainActivityClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * sets a new backgroundColor for the non-selected answers.
     */
    public void greyOutAnswersTextView() {
        for(int i = 0; i < answers.size(); i++) {
            answers.get(i).setBackgroundResource(R.color.lightgrey);
        }
    }

    /**
     * specifies what happens when an answer has been clicked.
     */
    public void OnAnswerClicked(View view) {
        animation.cancel();
        timeLeft = animation.getDuration();
        greyOutAnswersTextView();
        view.setBackgroundResource(R.color.green);
    }

    /**
     * populates the textView where the question is displayed.
     * @param question The question to be asked as a String.
     */
    public void populateQuestionTextView(String question) {
        TextView questionTextView = (TextView) findViewById(R.id.qQuestionTextView);
        questionTextView.setText(question);
    }

    /**
     * populates the different textViews for all 4 different answers.
     * @param answers is a List of Strings with alternative answers for the question.
     */
    public void populateAnswerTextViews(List<String> answers) {
        TextView[] qAnswersTextViews = {findViewById(R.id.qAnswer1TextView), findViewById(R.id.qAnswer2TextView), findViewById(R.id.qAnswer3TextView), findViewById(R.id.qAnswer4TextView)};
        for(int i = 0; i < qAnswersTextViews.length; i++){
            qAnswersTextViews[i].setText(answers.get(i));
        }
    }

    /**
     * Sets the current player1 name in "hotswap/hot seat" mode.
     * @param name
     */
    public void populatePlayerName(String name) {
        TextView playerName = findViewById(R.id.qHotSwapCaseTextView);
        playerName.setText(name);
    }

    /**
     *  Sets the questions number.
     * @param i equals to the number of the question asked, e.g. 4 out of 10 where 4 is i.
     */
    public void populateQuestionNum(int i) {
        TextView num = findViewById(R.id.qNumOfQuesTextView);
        num.setText(String.valueOf(i));
    }

    /**
     *  Sets the number of total questions.
     * @param i equals to the total number of questions in a series of questions.
     */
    public void populateTotalNumQuestions(int i) {
        TextView num = findViewById(R.id.textView9);
        num.setText(("of " + String.valueOf(i)));
    }

    /**
     * Starts a timer and visually presents it on the progressBar
     * @param progressBar the ProgressBar the timer acts on.
     * @param timer The total amount of time a question allows in seconds.
     */
    public void startTimer(ProgressBar progressBar, int timer) {
        progressBar.setMax(10000);
        animation = ObjectAnimator.ofInt(progressBar, "progress", 0, progressBar.getMax());
        animation.setDuration(timer * 1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        launchScorePageClass();
                    }
                }, 1000);
            }
        });
        animation.start();

    }

}

