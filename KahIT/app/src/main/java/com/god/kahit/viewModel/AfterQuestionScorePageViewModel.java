package com.god.kahit.viewModel;


import android.util.Log;
import android.util.Pair;

import com.god.kahit.Events.NewViewEvent;
import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Player;
import com.god.kahit.view.CategoryView;
import com.god.kahit.view.QuestionView;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class AfterQuestionScorePageViewModel extends ViewModel implements LifecycleObserver {
    private static final String LOG_TAG = AfterQuestionScorePageViewModel.class.getSimpleName();
    private Repository repository;

    public AfterQuestionScorePageViewModel() {
        repository = Repository.getInstance();
    }

    public List<Pair<String, String>> getScoreScreenContents() {
        List<Pair<String, String>> pairList = new ArrayList<>();
        for (Player player : Repository.getInstance().getPlayers()) {
            Pair<String, String> tuple = new Pair<>(player.getName(), Integer.toString(player.getScore()));
            pairList.add(tuple);
        }
        return pairList;
    }

    public boolean isRoundOver() {
        return Repository.getInstance().isRoundOver();
    }

    public boolean isHotSwap() {
        return repository.isHotSwap();
    }

    public boolean isHost() {
        return repository.isHost();
    }

    public void sendIsReady() {
        Log.d(LOG_TAG, "sendIsReady: called. Now waiting for server..");
        repository.setMyReadyStatus(true);
    }

    public void resetPlayersReady() {
        repository.resetPlayersReady();
    }

    public void showNextView() {
        Class<?> newViewClass;

        if (isRoundOver()) { //todo implement lottery
            newViewClass = CategoryView.class;
        } else {
            newViewClass = QuestionView.class;
        }

        repository.broadcastShowNewView(newViewClass);
        BUS.post(new NewViewEvent(newViewClass));
    }
}
