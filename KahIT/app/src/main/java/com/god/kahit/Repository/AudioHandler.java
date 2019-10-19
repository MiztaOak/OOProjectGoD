package com.god.kahit.Repository;

import android.content.Context;
import android.media.MediaPlayer;

import com.god.kahit.R;
import com.god.kahit.backgroundMusicService.MusicService;

public class AudioHandler {
    private MusicService mService;
    private boolean music;


    public AudioHandler(Context context) {
        mService = new MusicService(MediaPlayer.create(context, R.raw.pre_game_song));
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

    public void setMusicState(boolean music){
        this.music = music;
    }
}
