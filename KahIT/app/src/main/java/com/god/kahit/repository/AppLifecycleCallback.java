package com.god.kahit.repository;

interface AppLifecycleCallback {
    void onAppForegrounded();

    void onAppBackgrounded();

    void onAppTimedOut();
}
