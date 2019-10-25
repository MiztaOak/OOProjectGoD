package com.god.kahit.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;


public class AppLifecycleHandler implements LifecycleEventObserver {
    private static final String TAG = AppLifecycleHandler.class.getSimpleName();
    private static final long MAX_BACKGROUND_TIME = 10000; //10 sec

    private Timer onAppBackground_timer;
    private TimerTask onAppBackground_task;

    private AppLifecycleCallback appLifecycleCallback;
    private Context context;
    private boolean isActive;

    public AppLifecycleHandler(Context context, AppLifecycleCallback appLifecycleCallback) {
        this.context = context;
        this.appLifecycleCallback = appLifecycleCallback;
        isActive = false;
        setupObserver();
    }

    private void setupObserver() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event.equals(Lifecycle.Event.ON_START)) {
            Log.d(TAG, "onStateChanged: Application lifecycle onStart called - App was brought to foreground!");
            onAppForegrounded();
        } else if (event.equals(Lifecycle.Event.ON_STOP)) {
            Log.d(TAG, "onStateChanged: Application lifecycle onStop called - App was sent to background!");
            onAppBackgrounded();
        }
    }

    private void onAppForegrounded() {
        Log.i(TAG, "onAppForegrounded: called.");
        resetTimeoutTimer();
        appLifecycleCallback.onAppForegrounded();
    }

    private void onAppBackgrounded() {
        Log.i(TAG, "onAppBackgrounded: called.");
        appLifecycleCallback.onAppBackgrounded();

        if (isActive) {
            Log.i(TAG, "onAppBackgrounded: isActive = true, starting timeout timer");
            startTimeoutTimer();

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            Toast.makeText(context, String.format("KahIT: Timeout in %s sec", MAX_BACKGROUND_TIME / 1000),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onAppInactiveTimedOut() {
        Log.i(TAG, "onAppInactiveTimedOut: app timed out during inactivity.");
        resetTimeoutTimer();
        appLifecycleCallback.onAppTimedOut();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "KahIT: Timed out",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimeoutTimer() {
        Log.i(TAG, "startTimeoutTimer: called, starting timeout timer.");
        onAppBackground_timer = new Timer();
        onAppBackground_task = new TimerTask() {
            public void run() {
                onAppInactiveTimedOut();
            }
        };
        onAppBackground_timer.schedule(onAppBackground_task, MAX_BACKGROUND_TIME);
    }

    private void resetTimeoutTimer() {
        Log.i(TAG, "resetTimeoutTimer: called, resetting timeout timer.");
        if (onAppBackground_task != null) {
            onAppBackground_task.cancel();
        }
        if (onAppBackground_timer != null) {
            onAppBackground_timer.cancel();
        }
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
