package com.god.kahit.viewModel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;


public class PreGameCountdownViewModel extends ViewModel {
    private static final int TOAST_MESSAGE_TEXT_SIZE = 60;
    private static final String TOAST_MESSAGE_TEXT_COLOR = "#00CBF8";
    private Context context;
    private Toast toast;

    public PreGameCountdownViewModel(Context context) {
        this.context = context;
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

}
