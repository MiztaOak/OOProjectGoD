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
        if (isHost()) {
            return repository.getLocalPlayer().getId();
        } else {
            return repository.getClientPlayerId();
        }
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
}
