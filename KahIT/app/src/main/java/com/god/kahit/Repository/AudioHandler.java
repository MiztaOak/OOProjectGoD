package com.god.kahit.Repository;

import android.content.Context;
import android.media.MediaPlayer;

import com.god.kahit.R;
import com.god.kahit.backgroundMusicService.MusicService;

/**
 * A class that takes care of the audio in the app
 */
public class AudioHandler {
    private MusicService mService;
    private boolean music; //to know the current music state


    public AudioHandler(Context context) {
        mService = new MusicService(MediaPlayer.create(context, R.raw.nature3));
        mService.onCreate(); //calling onCreate to keep audioHandler updated with music settings
        music = true;
        //todo reading music state from database
    }

    public void startMusic() {
        mService.startMusic();

    }

    public void stopMusic() {
        mService.stopMusic();

    }

    public void resumeMusic() {
        mService.resumeMusic();

    }

    public void pauseMusic() {
        mService.pauseMusic();

    }

    public boolean getMusicState() {
        return music;
    }

    public void setMusicState(boolean music) {
        this.music = music;
    }
}
