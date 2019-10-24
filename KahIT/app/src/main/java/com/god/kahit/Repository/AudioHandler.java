package com.god.kahit.Repository;

import android.content.Context;
import android.media.MediaPlayer;

import com.god.kahit.R;
import com.god.kahit.backgroundMusicService.MusicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @responsibility: This class is responsible for the Audio in the game.
 * @used-by: Repository.
 * @author: Oussama Anadani
 */
class AudioHandler {
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
    private boolean musicState; // to know the current state of the musicState
    private Random random = new Random(); // to randomize the songs every time the app starts


    AudioHandler() {
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
        musicState = true; // default musicState
        //todo reading musicState state from database
    }


    /**
     * Creates songs in MediaPlayer
     *
     * @param list    of songs you want to create in MediaPlayer
     * @param context Context of which class
     */
    void startPlayList(Context context, List<Integer> list) {
        stopMusic();
        int rand = random.nextInt(list.size());//getting a random number in range list size
        musicService = new MusicService(MediaPlayer.create(context, list.get(rand)));
        musicService.musicSettings(); // to get song settings(looping, volume..)
        setMusicService(musicService); // to set the current song
        startMusic();
    }


    /**
     *  A method that starts music depending on the category name
     * @param context of the current class
     * @param categoryName name of the category that relates the the playlist
     */
    void startPlayList(Context context, String categoryName)
    {
        switch (categoryName) {
            case "science":
                startPlayList( context, sciencePlayList);
                break;
            case "history":
                startPlayList( context, historyPlayList);
                break;
            case "nature":
                startPlayList( context, naturePlayList);
                break;
            case "gaming":
                startPlayList( context, gamingPlayList);
                break;
            case "movies":
                startPlayList( context, moviesPlayList);
                break;
            case "religion":
                startPlayList( context, religionPlayList);
                break;
            case "sports":
                startPlayList( context, sportPlayList);
                break;
            case "test":
            case "mix":
            case "geography":
            case "language":
            case "literature":
            default:
                startPlayList( context, mixPlayList);
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
     * Changes the state of the music
     *
     * @param state music state
     */
    void setMusicState(boolean state) {
        this.musicState = state;
    }

    /**
     * @return music state
     */
    boolean getMusicState() {
        return musicState;
    }


    private void startMusic() {
        musicService.startMusic();
    }

    private void stopMusic() {
        musicService.stopMusic();
    }

    void resumeMusic() {
        musicService.resumeMusic();
    }

    void pauseMusic() {
        musicService.pauseMusic();
    }


    public MusicService getMusicService() {
        return musicService;
    }

    private void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }


    public List<Integer> getMixPlayList() {
        return mixPlayList;
    }

    public void setMixPlayList(List<Integer> mixPlayList) {
        this.mixPlayList = mixPlayList;
    }

    public List<Integer> getHistoryPlayList() {
        return historyPlayList;
    }

    public void setHistoryPlayList(List<Integer> historyPlayList) {
        this.historyPlayList = historyPlayList;
    }

    public List<Integer> getNaturePlayList() {
        return naturePlayList;
    }

    public void setNaturePlayList(List<Integer> naturePlayList) {
        this.naturePlayList = naturePlayList;
    }

    public List<Integer> getSciencePlayList() {
        return sciencePlayList;
    }

    public void setSciencePlayList(List<Integer> sciencePlayList) {
        this.sciencePlayList = sciencePlayList;
    }

    List<Integer> getPreGamePlayList() {
        return preGamePlayList;
    }

    public void setPreGamePlayList(List<Integer> preGamePlayList) {
        this.preGamePlayList = preGamePlayList;
    }

    public List<Integer> getSportPlayList() {
        return sportPlayList;
    }

    public void setSportPlayList(List<Integer> sportPlayList) {
        this.sportPlayList = sportPlayList;
    }

    public List<Integer> getCelebritiesPlayList() {
        return celebritiesPlayList;
    }

    public void setCelebritiesPlayList(List<Integer> celebritiesPlayList) {
        this.celebritiesPlayList = celebritiesPlayList;
    }

    public List<Integer> getMoviesPlayList() {
        return moviesPlayList;
    }

    public void setMoviesPlayList(List<Integer> moviesPlayList) {
        this.moviesPlayList = moviesPlayList;
    }
}
