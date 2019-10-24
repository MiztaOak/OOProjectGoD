package com.god.kahit.viewModel;

import android.content.Context;

import com.god.kahit.Repository.Repository;

import androidx.lifecycle.ViewModel;

public class ChooseGameViewModel extends ViewModel {

    public ChooseGameViewModel() {
    }

    public void resetApp(Context context) {
        Repository.getInstance().resetApp(context);
    }
}
