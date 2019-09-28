package com.god.kahit.ViewModel;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.Repository;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuizListener;
import com.god.kahit.view.QuestionClass;

import java.util.List;

public class QuestionViewModel extends ViewModel implements LifecycleObserver, QuizListener {
    private static final String TAG = QuestionClass.class.getSimpleName();

    private MutableLiveData<String> questionText;
    private MutableLiveData<List<String>> questionAlts;
    private MutableLiveData<Integer> questionTime;
    private Question currentQuestion;
    private boolean isQuestionAnswered;

    private boolean correctAnswerWasGiven = false;

    public QuestionViewModel() {
        Repository.getInstance().addQuizListener(this);
        Repository.getInstance().startGame();
    }

    public MutableLiveData<String> getQuestionText(){
        if(questionText == null){
            questionText = new MutableLiveData<>();
        }
        return questionText;
    }

    public MutableLiveData<List<String>> getQuestionAlts(){
        if(questionAlts == null){
            questionAlts = new MutableLiveData<>();
        }
        return questionAlts;
    }

    public MutableLiveData<Integer> getQuestionTime(){
        if(questionTime == null){
            questionTime = new MutableLiveData<>();
        }
        return questionTime;
    }

    public void nextQuestion(){
        Repository.getInstance().nextQuestion();
    }

    @Override
    public void receiveQuestion(Question q) {
        currentQuestion = q;
        isQuestionAnswered = false;
        questionText.setValue(q.getQuestion());
        questionAlts.setValue(q.getAlternatives());
        questionTime.setValue(currentQuestion.getTime());
    }

    public void onAnswerClicked(View view, ObjectAnimator animation, List<TextView> answers){
        if(!isQuestionAnswered) {
            animation.cancel();
            String alternative = answers.get(answers.indexOf(view)).getText().toString();
            long timeLeft = animation.getDuration();
            greyOutAnswersTextView(answers);
            if (currentQuestion.isCorrectAnswer(alternative)) {
                view.setBackgroundResource(R.color.green);
            } else {
                view.setBackgroundResource(R.color.red);
            }
            Repository.getInstance().sendAnswer(alternative, currentQuestion, timeLeft);
            isQuestionAnswered = true;
        }
    }

    /**
     * sets a new backgroundColor for the non-selected answers.
     */
    public void greyOutAnswersTextView( List<TextView> answers) {
        for(int i = 0; i < answers.size(); i++) {
            answers.get(i).setBackgroundResource(R.color.lightgrey);
        }
    }

    public boolean isQuestionAnswered() {
        return isQuestionAnswered;
    }
}
