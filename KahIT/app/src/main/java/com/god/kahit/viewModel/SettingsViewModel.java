package com.god.kahit.viewModel;

import com.god.kahit.repository.Repository;

import androidx.lifecycle.ViewModel;

/**
 * responsibility: ViewModel for the settingsView. Handles the communication with the repository.
 * used-by: LobbyNetView.
 *
 * @author Jakob Ewerstrand
 */
public class SettingsViewModel extends ViewModel {
    private static final String LOG_TAG = SettingsViewModel.class.getSimpleName();

    public SettingsViewModel() {
    }

    /**
     * Turns off the music by calling repository.
     */
    public void turnOffMusic() {
        Repository.getInstance().pauseMusic();
        Repository.getInstance().setMusicState(false);
    }

    /**
     * Turns on the music by calling repository.
     */
    public void turnOnMusic() {
        Repository.getInstance().resumeMusic();
        Repository.getInstance().setMusicState(true);
    }
}
