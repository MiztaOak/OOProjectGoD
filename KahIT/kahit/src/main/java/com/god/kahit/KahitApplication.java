package com.god.kahit;

import android.app.Application;

import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.databaseService.QuestionDataLoaderRealtime;
import com.god.kahit.model.ItemFactory;
import com.god.kahit.model.QuestionFactory;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Class that currently only exists so that the setPersistenceEnabled call is called first to prevent
 * a crash from happening.
 */
public class KahitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
