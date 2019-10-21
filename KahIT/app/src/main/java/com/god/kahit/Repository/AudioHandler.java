package com.god.kahit.Repository;
import android.content.Context;
import android.media.MediaPlayer;
import com.god.kahit.R;
import com.god.kahit.backgroundMusicService.MusicService;

/**
 * @responsibility: This class is responsible for the Audio in the game.
 * <p>
 * used-by: This class is used in the following classes:
 * Repository.
 * @author: Oussama Anadani
 */
class AudioHandler {
    private MusicService mService;
    private boolean music; // to know the current state of the music


    AudioHandler(Context context) {
        mService = new MusicService(MediaPlayer.create(context, R.raw.pre_game_song));
        mService.onCreate(); // to get music settings(looping, volume..)
        music = true;
        //todo reading music state from database
    }

    void startMusic() {
        mService.startMusic();

    }

    void stopMusic() {
        mService.stopMusic();

    }

    void resumeMusic() {
        mService.resumeMusic();

    }

    void pauseMusic() {
        mService.pauseMusic();

    }

    boolean getMusicState() {
        return music;
    }

    void setMusicState(boolean music){
        this.music = music;
    }
}
