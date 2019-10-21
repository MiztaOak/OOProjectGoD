package com.god.kahit.viewModel;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.god.kahit.Events.NewViewEvent;
import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Player;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuizListener;
import com.god.kahit.view.AfterQuestionScorePageView;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.Events.EventBusGreenRobot.BUS;

public class QuestionViewModel extends ViewModel implements LifecycleObserver, QuizListener {
    private static final String LOG_TAG = QuestionViewModel.class.getSimpleName();
    private Repository repository;
    private MutableLiveData<String> questionText;
    private MutableLiveData<List<String>> questionAlts;
    private MutableLiveData<Integer> questionTime;
    private MutableLiveData<String> playerName;
    private Question currentQuestion;

    private boolean isCorrectAnswer = false;

    private int numOfRepeats = 0;

    public QuestionViewModel() {
        repository = Repository.getInstance();
        repository.addQuizListener(this);
        if (repository.isRoundOver()) {
            repository.startGame();
        }
    }

    /**
     * Method that request a new question from model
     */
    public void nextQuestion() {
        repository.nextQuestion();
    }

    public boolean isHotSwap() {
        return repository.isHotSwap();
    }

    public boolean isHost() {
        return repository.isHost();
    }

    public boolean isMe(Player player) {
        return repository.isMe(player.getId());
    }

    /**
     * Method that receives a question from the model using the quizListener interface
     *
     * @param q - the question that is being received
     */
    @Override
    public void receiveQuestion(Question q, int n) {
        currentQuestion = q;
        numOfRepeats = n;
        isCorrectAnswer = false;
        questionText.setValue(q.getQuestion());
        questionAlts.setValue(q.getAlternatives());
        questionTime.setValue(currentQuestion.getTime());

        playerName.setValue(repository.getCurrentPlayerName());
    }

    /**
     * Method that is run when the user presses one of the alternatives in the questionActivity
     *
     * @param view      - the view that the user pressed
     * @param animation - the animation of the progressbar
     * @param answers   - a list with all of the alternative buttons
     */
    public void onAnswerClicked(View view, ObjectAnimator animation, List<TextView> answers) {
        String alternative = answers.get(answers.indexOf(view)).getText().toString();
        long timeLeft = animation.getDuration() - animation.getCurrentPlayTime();

        if (currentQuestion.isCorrectAnswer(alternative)) {
            isCorrectAnswer = true;
        }
        repository.sendAnswer(alternative, currentQuestion, timeLeft / 1000);
    }


    public String getMyPlayerId() {
        return repository.getCurrentPlayer().getId();
    }

    public void sendIsReady() {
        Log.d(LOG_TAG, "sendIsReady: called. Now waiting for server..");
        repository.setMyReadyStatus(true);
    }

    public void resetPlayersReady() {
        repository.resetPlayersReady();
    }

    public void showNextView() {
        Class<?> newViewClass = AfterQuestionScorePageView.class; //todo get actual next view, it's not always AfterQuestionScorePageView
        repository.broadcastShowNewView(newViewClass);
        BUS.post(new NewViewEvent(newViewClass));
    }

    public boolean isMoveOn() {
        numOfRepeats--;
        return numOfRepeats <= 0;
    }

    public void repeatQuestion() {
        isCorrectAnswer = false;
        repository.incrementCurrentPlayer();
        playerName.setValue(repository.getCurrentPlayerName());
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

    public MutableLiveData<String> getPlayerName() {
        if (playerName == null) {
            playerName = new MutableLiveData<>();
        }
        return playerName;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public boolean isAutoAnswer(){
        return Repository.getInstance().isAutoAnswer();
    }
    /**
     * A method that checks if the current player has the fifty fifty buff.
     *
     * @return : boolean which indicates if a player has the buff or not.
     */
    public boolean halfTheAlternatives(){
        return Repository.getInstance().halfTheAlternatives();
    }

    /**
     * A method that checks which answer is the right answer for a question
     *
     * @return : An int which is the index of the answer;
     */
    public int getAnswerIndex(){
        for (int i = 1; i < Objects.requireNonNull(questionAlts.getValue()).size(); i++) {
            if(currentQuestion.getAnswer().equals(questionAlts.getValue().get(i))){
                return i;
            }
        }
        return 0;
    }

    /**
     * A method that randomizes an index of answer
     * @return : An int which will be the index of the answer, between 0 and 3
     */
    public int autoChooseAnswer(){
        return (int) (Math.random()*3);
    }
}
