package com.god.kahit.ViewModel;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.Repository;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuizListener;
import com.god.kahit.view.QuestionClass;

import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuestionViewModel extends ViewModel implements LifecycleObserver, QuizListener {
    private static final String TAG = QuestionClass.class.getSimpleName();

    private MutableLiveData<String> questionText;
    private MutableLiveData<List<String>> questionAlts;
    private MutableLiveData<Integer> questionTime;
    private Question currentQuestion;
    private boolean isQuestionAnswered;

    private int indexOfClickedView = -1;
    private boolean correctAnswerWasGiven = false;

    public QuestionViewModel() {
        Repository.getInstance().addQuizListener(this);
        if (Repository.getInstance().isRoundOver()) {
            Repository.getInstance().startGame();
        }
    }

    public MutableLiveData<String> getQuestionText() {
        if (questionText == null) {
            questionText = new MutableLiveData<>();
        }
        return questionText;
    }

    public MutableLiveData<List<String>> getQuestionAlts() {
        if (questionAlts == null) {
            questionAlts = new MutableLiveData<>();
        }
        return questionAlts;
    }

    public MutableLiveData<Integer> getQuestionTime() {
        if (questionTime == null) {
            questionTime = new MutableLiveData<>();
        }
        return questionTime;
    }

    /**
     * Method that request a new question from model
     */
    public void nextQuestion() {
        Repository.getInstance().nextQuestion();
    }

    /**
     * Method that receives a question from the model using the quizListener interface
     *
     * @param q - the question that is being received
     */
    @Override
    public void receiveQuestion(Question q) {
        currentQuestion = q;
        isQuestionAnswered = false;
        correctAnswerWasGiven = false;
        indexOfClickedView = -1;
        questionText.setValue(q.getQuestion());
        questionAlts.setValue(q.getAlternatives());
        questionTime.setValue(currentQuestion.getTime());
    }

    /**
     * Method that is run when the user presses one of the alternatives in the questionActivity
     *
     * @param view      - the view that the user pressed
     * @param animation - the animation of the progressbar
     * @param answers   - a list with all of the alternative buttons
     */
    public void onAnswerClicked(View view, ObjectAnimator animation, List<TextView> answers) {
        if (!isQuestionAnswered) {
            String alternative = answers.get(answers.indexOf(view)).getText().toString();
            long timeLeft = animation.getDuration();
            greyOutAnswersTextView(answers);
            indexOfClickedView = answers.indexOf(view);
            answers.get(indexOfClickedView).setBackgroundResource(R.color.blue);
            if (currentQuestion.isCorrectAnswer(alternative)) {
                correctAnswerWasGiven = true;
            }
            Repository.getInstance().sendAnswer(alternative, currentQuestion, timeLeft);
            isQuestionAnswered = true;
        }
    }

    /**
     * sets a new backgroundColor for the non-selected answers.
     */
    private void greyOutAnswersTextView(List<TextView> answers) {
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setBackgroundResource(R.color.lightgrey);
        }
    }

    public void updateViewForBeginningOfAnimation(final Animator animation, final List<TextView> answers) {
        greyOutAnswersTextView(answers);
        if (indexOfClickedView >= 0) {
            if (correctAnswerWasGiven) {
                answers.get(indexOfClickedView).setBackgroundResource(R.color.green);
            } else {
                answers.get(indexOfClickedView).setBackgroundResource(R.color.red);
            }
        }
    }

    private void resetColorOfTextView(List<TextView> answers) {
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setBackgroundResource(R.color.colorPrimary);
        }
    }
}
