package com.god.kahit.ViewModel;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

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
    private Question currentQuestion;

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

    public void nextQuestion(){
        Repository.getInstance().nextQuestion();
    }

    @Override
    public void receiveQuestion(Question q) {
        currentQuestion = q;
        questionText.setValue(q.getQuestion());
        questionAlts.setValue(q.getAlternatives());
    }

    public void onAnswerClicked(View view, ObjectAnimator animation, String alternative){
        animation.cancel();
        long timeLeft = animation.getDuration();
        view.setBackgroundResource(R.color.green);
        Repository.getInstance().sendAnswer(alternative,currentQuestion,timeLeft);
    }
}
