package com.god.kahit.viewModel;

import com.god.kahit.Repository.Repository;

import androidx.lifecycle.ViewModel;

public class ChooseGameViewModel extends ViewModel {

    public ChooseGameViewModel() {
    }

    public void resetApp() {
        Repository.getInstance().resetApp();
    }
}
