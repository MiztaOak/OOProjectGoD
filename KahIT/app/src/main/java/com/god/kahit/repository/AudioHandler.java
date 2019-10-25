package com.god.kahit.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

import com.god.kahit.R;
import com.god.kahit.backgroundMusicService.MusicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * responsibility: This class is responsible for the Audio in the game.
 * used-by: Repository.
 * @author: Oussama Anadani, Mats Cedervall
 */
class AudioHandler {
    private static final String TAG = AudioHandler.class.getSimpleName();

    private List<Integer> historyPlayList;
    private List<Integer> naturePlayList;
    private List<Integer> sciencePlayList;
    private List<Integer> preGamePlayList;
    private List<Integer> mixPlayList;
    private List<Integer> sportPlayList;
    private List<Integer> celebritiesPlayList;
    private List<Integer> moviesPlayList;
    private List<Integer> gamingPlayList;
    private List<Integer> religionPlayList;

    private MusicService musicService;
    private List<Integer> currentPlaylist;
    private boolean musicState; // to know the current state of the musicState
    private Random random = new Random(); // to randomize the songs every time the app starts

    AudioHandler(Context context) {
        historyPlayList = new ArrayList<>();
        naturePlayList = new ArrayList<>();
        sciencePlayList = new ArrayList<>();
        preGamePlayList = new ArrayList<>();
        mixPlayList = new ArrayList<>();
        sportPlayList = new ArrayList<>();
        celebritiesPlayList = new ArrayList<>();
        moviesPlayList = new ArrayList<>();
        gamingPlayList = new ArrayList<>();
        religionPlayList = new ArrayList<>();


        //Adding all songs to their related list
        addSongsToList();

        setupMusicState(context);

        if (musicState) {
            startPlaylist(context, preGamePlayList);
        } else {
            setPlaylist(context, preGamePlayList);
        }
    }

    /**
     * A method used to set a different playlist of songs.
     *
     * @param playlist playlist of songs you want to prepare
     * @param context  Context used to create a new MediaPlayer
     */
    private void setPlaylist(Context context, List<Integer> playlist) {
        if (currentPlaylist != playlist) {
            currentPlaylist = playlist;
            int rand = random.nextInt(playlist.size());//getting a random number in range list size
            musicService = new MusicService(MediaPlayer.create(context, playlist.get(rand)));
            musicService.musicSettings(); // to get song settings(looping, volume..)
            setMusicService(musicService); // to set the current song
        } else {
            Log.i(TAG, "setPlaylist: called. currentPlaylist is the same as the new one, skipping call.");
        }
    }

    /**
     * A method used to start playing different playlists of songs.
     * Will not start another playlist if it is the same as the currently playing playlist.
     * Stops the currently playing music automatically.
     *
     * @param playlist playlist of songs you want to play
     * @param context  Context used to create a new MediaPlayer
     */
    void startPlaylist(Context context, List<Integer> playlist) {
        if (currentPlaylist != playlist && musicState) {
            if (musicService != null) {
                stopMusic();
            }
            setPlaylist(context, playlist);
            startMusic();
        } else {
            Log.i(TAG, "startPlaylist: called. currentPlaylist is the same as the new one, skipping call.");
        }
    }


    /***
     * A method used to load the stored music state from SharedPreferences.
     * @param context context used to get sharedPreferences
     */
    private void setupMusicState(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("kahit", Context.MODE_PRIVATE);  // getting the last state of the switchButton
        musicState = sharedpreferences.getBoolean("musicOn", true);  //set checked(on) as a default case for the switchButton
    }


    /**
     * A method that starts music depending on the category name
     *
     * @param context      of the current class
     * @param categoryName name of the category that relates the the playlist
     */
    void startPlaylist(Context context, String categoryName) {
        switch (categoryName) {
            case "science":
                startPlaylist(context, sciencePlayList);
                break;
            case "history":
                startPlaylist(context, historyPlayList);
                break;
            case "nature":
                startPlaylist(context, naturePlayList);
                break;
            case "gaming":
                startPlaylist(context, gamingPlayList);
                break;
            case "movies":
                startPlaylist(context, moviesPlayList);
                break;
            case "religion":
                startPlaylist(context, religionPlayList);
                break;
            case "sports":
                startPlaylist(context, sportPlayList);
                break;
            case "test":
            case "mix":
            case "geography":
            case "language":
            case "literature":
            default:
                startPlaylist(context, mixPlayList);
                break;
        }
    }

    /**
     * Adds all songs to their related list
     */
    private void addSongsToList() {
        addPreGameSongs();
        addHistorySongs();
        addMixSongs();
        addNatureSongs();
        addScienceSongs();
        addSportSongs();
        addCelebritiesSongs();
        addMoviesSongs();
        addGamingSongs();
        addReligionSongs();
    }

    /**
     * Adds religion songs to religionPlaylist
     */
    private void addReligionSongs() {
        religionPlayList.add(R.raw.religion);
        religionPlayList.add(R.raw.religion2);
        religionPlayList.add(R.raw.religion3);
        religionPlayList.add(R.raw.religion4);
        religionPlayList.add(R.raw.religion5);
        religionPlayList.add(R.raw.religion6);

    }

    /**
     * Adds gaming songs to gamingPlayList
     */
    private void addGamingSongs() {
        gamingPlayList.add(R.raw.gaming);
        gamingPlayList.add(R.raw.gaming1);
        gamingPlayList.add(R.raw.gaming3);
        gamingPlayList.add(R.raw.gaming4);
        gamingPlayList.add(R.raw.gaming5);
    }

    /**
     * Adds movies songs to moviesPlayList
     */
    private void addMoviesSongs() {
        moviesPlayList.add(R.raw.movies);
        moviesPlayList.add(R.raw.movies2);
        moviesPlayList.add(R.raw.movies3);
        moviesPlayList.add(R.raw.movies4);
        moviesPlayList.add(R.raw.movies5);
        moviesPlayList.add(R.raw.movies6);
        moviesPlayList.add(R.raw.movies7);
        moviesPlayList.add(R.raw.movies8);

    }

    /**
     * Adds celebrities songs to celebritiesPlayList
     */
    private void addCelebritiesSongs() {
        celebritiesPlayList.add(R.raw.celebrities);
        celebritiesPlayList.add(R.raw.celebrities2);
        celebritiesPlayList.add(R.raw.celebrities3);
        celebritiesPlayList.add(R.raw.celebrities4);
        celebritiesPlayList.add(R.raw.celebrities5);
        celebritiesPlayList.add(R.raw.celebrities6);
    }

    /**
     * Adds sport songs to sportPlayList
     */
    private void addSportSongs() {
        sportPlayList.add(R.raw.sport1);
        sportPlayList.add(R.raw.sport2);
        sportPlayList.add(R.raw.sport3);
    }


    /**
     * Adds mix songs to mixPlayList
     */
    private void addMixSongs() {
        mixPlayList.add(R.raw.mix);
        mixPlayList.add(R.raw.mix2);
        mixPlayList.add(R.raw.mix3);
        mixPlayList.add(R.raw.mix4);
        mixPlayList.add(R.raw.mix5);
        mixPlayList.add(R.raw.mix6);
        mixPlayList.add(R.raw.mix7);
        mixPlayList.add(R.raw.mix8);
        mixPlayList.add(R.raw.mix9);
        mixPlayList.add(R.raw.mix10);
        mixPlayList.add(R.raw.mix11);
        mixPlayList.add(R.raw.mix12);
        mixPlayList.add(R.raw.mix13);
        mixPlayList.add(R.raw.mix14);
        mixPlayList.add(R.raw.mix15);

    }

    /**
     * Adds history songs to historyPlayList
     */

    private void addHistorySongs() {
        historyPlayList.add(R.raw.history1);
        historyPlayList.add(R.raw.history2);
        historyPlayList.add(R.raw.history3);
    }


    /**
     * Adds nature songs to naturePlayList
     */
    private void addNatureSongs() {
        naturePlayList.add(R.raw.nature);
        naturePlayList.add(R.raw.nature2);
        naturePlayList.add(R.raw.nature3);
        naturePlayList.add(R.raw.nature4);
    }


    /**
     * Adds preGame songs to preGamePlayList
     */
    private void addPreGameSongs() {
        preGamePlayList.add(R.raw.pre_game_song);
        preGamePlayList.add(R.raw.pre_game_song2);
        preGamePlayList.add(R.raw.pre_game_song3);
        preGamePlayList.add(R.raw.pre_game_song4);
        preGamePlayList.add(R.raw.pre_game_song5);
        preGamePlayList.add(R.raw.pre_game_song6);
        preGamePlayList.add(R.raw.pre_game_song7);
    }

    /**
     * Adds science songs to sciencePlayList
     */
    private void addScienceSongs() {
        sciencePlayList.add(R.raw.science);
        sciencePlayList.add(R.raw.science2);
        sciencePlayList.add(R.raw.science3);
        sciencePlayList.add(R.raw.science4);
    }

    /**
     * @return music state
     */
    boolean getMusicState() {
        return musicState;
    }

    /**
     * Changes the state of the music
     *
     * @param state music state
     */
    void setMusicState(boolean state) {
        this.musicState = state;
    }

    private void startMusic() {
        if (musicService != null) {
            musicService.startMusic();
        } else {
            Log.i(TAG, "startMusic: attempt to call startMusic on null musicService, ignoring call");
        }
    }

    private void stopMusic() {
        if (musicService != null) {
            musicService.stopMusic();
        } else {
            Log.i(TAG, "stopMusic: attempt to call stopMusic on null musicService, ignoring call");
        }
    }

    void resumeMusic() {
        if (musicService != null) {
            musicService.resumeMusic();
        } else {
            Log.i(TAG, "resumeMusic: attempt to call resumeMusic on null musicService, ignoring call");
        }
    }

    void pauseMusic() {
        if (musicService != null) {
            musicService.pauseMusic();
        } else {
            Log.i(TAG, "pauseMusic: attempt to call pauseMusic on null musicService, ignoring call");
        }
    }

    private void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    List<Integer> getMixPlayList() {
        return mixPlayList;
    }

    List<Integer> getHistoryPlayList() {
        return historyPlayList;
    }

    List<Integer> getNaturePlayList() {
        return naturePlayList;
    }

    List<Integer> getSciencePlayList() {
        return sciencePlayList;
    }

    List<Integer> getPreGamePlayList() {
        return preGamePlayList;
    }

    List<Integer> getSportPlayList() {
        return sportPlayList;
    }

    List<Integer> getCelebritiesPlayList() {
        return celebritiesPlayList;
    }

    List<Integer> getMoviesPlayList() {
        return moviesPlayList;
    }
}
