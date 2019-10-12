package com.god.kahit.viewModel;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;


public class PreGameCountdownViewModel extends ViewModel {

    private Context c;
    private Toast toast;

    public PreGameCountdownViewModel(Context context) {
        c = context;
    }

    /**
     * Toast start message
     */
    public void startToastMessage() {
        toast = Toast.makeText(c, "Get ready..", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 100);
        View view = toast.getView();
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(40);
        view.setBackgroundColor(Color.TRANSPARENT);
        toast.show();

    }

    /**
     * Toast finish message
     */
    public void finishToastMessage() {
        toast = Toast.makeText(c, "GO!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 100);
        View view = toast.getView();
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(40);
        view.setBackgroundColor(Color.TRANSPARENT);
        toast.show();

    }

}
