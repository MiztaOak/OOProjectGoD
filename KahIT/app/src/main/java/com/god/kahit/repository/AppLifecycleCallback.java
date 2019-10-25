package com.god.kahit.repository;

public interface AppLifecycleCallback {
    void onAppForegrounded();

    void onAppBackgrounded();

    void onAppTimedOut();
}
