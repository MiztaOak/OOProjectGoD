package com.god.kahit.Repository;

public interface AppLifecycleCallback {
    void onAppForegrounded();

    void onAppBackgrounded();

    void onAppTimedOut();
}
