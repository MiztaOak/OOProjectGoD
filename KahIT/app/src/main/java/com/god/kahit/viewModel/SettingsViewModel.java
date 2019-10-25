package com.god.kahit.viewModel;

import androidx.lifecycle.ViewModel;

import com.god.kahit.Repository.Repository;

public class SettingsViewModel extends ViewModel {
    private static final String LOG_TAG = SettingsViewModel.class.getSimpleName();

    public SettingsViewModel() {
    }

    public void turnOffMusic() {
        Repository.getInstance().pauseMusic();
        Repository.getInstance().setMusicState(false);
    }

    public void turnOnMusic() {
        Repository.getInstance().resumeMusic();
        Repository.getInstance().setMusicState(true);
    }
}
