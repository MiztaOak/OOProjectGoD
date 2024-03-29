package com.god.kahit.viewModel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.god.kahit.applicationEvents.NewViewEvent;
import com.god.kahit.repository.Repository;
import com.god.kahit.view.QuestionView;

import androidx.lifecycle.ViewModel;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * responsibility: A class that places a countdown timer in center of the layout
 * used-by: PreGameCountdownView.
 *
 * @author Oussama Anadani, Mats Cedervall
 */
public class PreGameCountdownViewModel extends ViewModel {
    private static final int TOAST_MESSAGE_TEXT_SIZE = 60;
    private static final String TOAST_MESSAGE_TEXT_COLOR = "#00CBF8";
    private final Repository repository;
    private Context context;
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

    public boolean isHotSwap() {
        return repository.isHotSwap();
    }

    public void sendIsReady() {
        repository.setMyReadyStatus(true);
    }

    public String getMyPlayerId() {
        return repository.getCurrentPlayer().getId();
    }

    public void resetPlayersReady() {
        repository.resetPlayersReady();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Initiates the questionView.
     */
    public void showNextView() {
        Class<?> newViewClass = QuestionView.class;
        repository.broadcastShowNewView(newViewClass);
        BUS.post(new NewViewEvent(newViewClass));
    }
}
