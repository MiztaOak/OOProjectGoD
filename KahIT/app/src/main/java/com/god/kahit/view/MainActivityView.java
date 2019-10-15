package com.god.kahit.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;


import com.god.kahit.R;
import com.god.kahit.Repository;
import com.god.kahit.backgroundMusicService.MusicService;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityView extends AppCompatActivity {
    private static final String LOG_TAG = MainActivityView.class.getSimpleName();

    private boolean mIsBound = false;
    private MusicService mServ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.main_activity);
        Repository.getInstance().startNewGameInstance(getApplicationContext());

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

    }

    public void launchChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
//        Intent intent = new Intent(this, ChooseGameView.class);
        Intent intent = new Intent(this, ChooseGameView.class);
        startActivity(intent);
    }
    public void launchSettingsView(View view) {
        Log.d(LOG_TAG, "Button clicked!");
//        Intent intent = new Intent(this, ChooseGameClass.class);
        Intent intent = new Intent(this, SettingsView.class);
        startActivity(intent);
    }
    public void launchPreGameCountdownView(View view) {
        Log.d(LOG_TAG, "Button clicked!");
//        Intent intent = new Intent(this, ChooseGameClass.class);
        Intent intent = new Intent(this, PreGameCountdownView.class);
        startActivity(intent);
    }
    public void launchAboutKahitView(View view) {
        Log.d(LOG_TAG, "Button clicked!");
//        Intent intent = new Intent(this, ChooseGameClass.class);
        Intent intent = new Intent(this, AboutKahitView.class);
        startActivity(intent);
    }

   // public void onPause() { super.onPause(); if (mp != null) mp.pause(); }
  /* @Override
   protected void onResume() {
       if(mp != null && !mp.isPlaying())
           mp.start();
       super.onResume();
   }*/

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }


}
