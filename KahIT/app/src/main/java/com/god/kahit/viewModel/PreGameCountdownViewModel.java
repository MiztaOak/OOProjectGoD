package com.god.kahit.viewModel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.god.kahit.Events.NewViewEvent;
import com.god.kahit.Repository.Repository;
import com.god.kahit.view.QuestionView;

import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;


public class PreGameCountdownViewModel extends ViewModel {
    private static final int TOAST_MESSAGE_TEXT_SIZE = 60;
    private static final String TOAST_MESSAGE_TEXT_COLOR = "#00CBF8";
    private Context context;
    private Repository repository;
    private Toast toast;

    public PreGameCountdownViewModel() {
        repository = Repository.getInstance();
    }

    /**
     * Toast start message
     */
    public void startToastMessage() {
        toast = Toast.makeText(context, "Get ready..", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 100);
        View view = toast.getView();
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(TOAST_MESSAGE_TEXT_SIZE);
        messageTextView.setTypeface(messageTextView.getTypeface(), Typeface.BOLD);
        messageTextView.setTextColor(Color.parseColor(TOAST_MESSAGE_TEXT_COLOR));
        view.setBackgroundColor(Color.TRANSPARENT);
        toast.show();
    }

    /**
     * Toast finish message
     */
    public void finishToastMessage() {
        toast = Toast.makeText(context, "GO!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 100);
        View view = toast.getView();
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(TOAST_MESSAGE_TEXT_SIZE);
        messageTextView.setTypeface(messageTextView.getTypeface(), Typeface.BOLD);
        messageTextView.setTextColor(Color.parseColor(TOAST_MESSAGE_TEXT_COLOR));
        view.setBackgroundColor(Color.TRANSPARENT);
        toast.show();
    }

    public boolean isHost() {
        return repository.isHost();
    }

    public void sendIsReady() {
        repository.setMyReadyStatus(true);
    }

    public String getMyPlayerId() {
        if(isHost()) {
            return repository.getHostPlayerId();
        }else {
            return repository.getClientPlayerId();
        }
    }

    public void resetPlayersReady() {
        repository.resetPlayersReady();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void showNextView() {
        Class<?> newViewClass = QuestionView.class;
        repository.broadcastShowNewView(newViewClass);
        BUS.post(new NewViewEvent(newViewClass));
    }
}
