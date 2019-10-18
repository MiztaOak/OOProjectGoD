package com.god.kahit.Repository;

import android.content.Context;
import android.media.MediaPlayer;

import com.god.kahit.R;
import com.god.kahit.backgroundMusicService.MusicService;

public class AudioHandler {
    private MusicService mService;

    public AudioHandler(Context context){
        mService = new MusicService(MediaPlayer.create(context, R.raw.pre_game_song));
    }

    public void startMusic(){
        mService.startMusic();
    }

    public void stopMusic(){
        mService.stopMusic();
    }

    public void resumeMusic(){
        mService.resumeMusic();
    }

    public void pauseMusic(){
        mService.pauseMusic();
    }
}
