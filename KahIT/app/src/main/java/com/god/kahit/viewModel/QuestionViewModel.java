package com.god.kahit.viewModel;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.god.kahit.applicationEvents.NewViewEvent;
import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Player;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuizListener;
import com.god.kahit.view.AfterQuestionScorePageView;

import java.util.List;
import java.util.Objects;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

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
     * @param index     - the view that the user pressed
     * @param animation - the animation of the progressbar
     */
    public void onAnswerClicked(int index, ObjectAnimator animation) {
        String alternative = getAnswerString(index);
        long timeLeft = animation.getDuration() - animation.getCurrentPlayTime();

        if (currentQuestion.isCorrectAnswer(alternative)) {
            isCorrectAnswer = true;
        }
        repository.sendAnswer(alternative, currentQuestion, timeLeft / 1000);
    }

    private String getAnswerString(int index) {
        if (questionAlts.getValue() != null) {
            return questionAlts.getValue().get(index);
        } else {
            Log.d(LOG_TAG, "getAnswerString: Attempt to call size on null questionAlts.getValue(), returning '' ");
            return "";
        }
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

    public boolean isAutoAnswer() {
        return Repository.getInstance().isAutoAnswer();
    }

    /**
     * A method that checks if the current player has the fifty fifty buff.
     *
     * @return : boolean which indicates if a player has the buff or not.
     */
    public boolean isHalfTheAlternatives() {
        return Repository.getInstance().isHalfTheAlternatives();
    }

    /**
     * A method that checks which answer is the right answer for a question
     *
     * @return : An int which is the index of the answer;
     */
    private int getAnswerIndex() {
        for (int i = 1; i < Objects.requireNonNull(questionAlts.getValue()).size(); i++) {
            if (currentQuestion.getAnswer().equals(questionAlts.getValue().get(i))) {
                return i;
            }
        }
        return 0;
    }

    /**
     * A method that calculates two indexes out of a given size of a list
     *
     * @param size
     * @return
     */
    public Pair<Integer, Integer> getTwoIndexes(int size) {
        int firstValue = (getAnswerIndex() + 1) % size;
        int secondValue = (getAnswerIndex() - 1) % size;
        return new Pair<>(firstValue, secondValue);
    }

    /**
     * A method that randomizes an index of answer
     *
     * @return : An int which will be the index of the answer, between 0 and 3
     */
    public int autoChooseAnswer() {
        if (questionAlts.getValue() != null) {
            return (int) (Math.random() * (questionAlts.getValue()).size());
        } else {
            Log.i(LOG_TAG, "autoChooseAnswer: Attempt to call size on null questionAlts.getValue(), returning 0");
            return 0;
        }
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void setQuestionText(MutableLiveData<String> questionText) {
        this.questionText = questionText;
    }

    public void setQuestionAlts(MutableLiveData<List<String>> questionAlts) {
        this.questionAlts = questionAlts;
    }

    public void setQuestionTime(MutableLiveData<Integer> questionTime) {
        this.questionTime = questionTime;
    }

    public void setPlayerName(MutableLiveData<String> playerName) {
        this.playerName = playerName;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }

    public void setNumOfRepeats(int numOfRepeats) {
        this.numOfRepeats = numOfRepeats;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }
}
