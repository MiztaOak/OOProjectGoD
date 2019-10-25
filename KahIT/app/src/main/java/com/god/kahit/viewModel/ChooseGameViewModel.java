package com.god.kahit.viewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.god.kahit.Repository.Repository;

/**
 * responsibility: ViewModel for the ChooseGameView.
 * <p>
 * used-by: ChooseGameView.
 *
 * @author Mats Cedervall
 */
public class ChooseGameViewModel extends ViewModel {

    public ChooseGameViewModel() {
    }

    /**
     * Resets QuizGame, playerManager and network.
     *
     * @param context - given context.
     */
    public void resetApp(Context context) {
        Repository.getInstance().resetApp(context);
    }
}
