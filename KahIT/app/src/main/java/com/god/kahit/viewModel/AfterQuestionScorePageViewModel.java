package com.god.kahit.viewModel;


import android.util.Pair;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Player;

import java.util.ArrayList;
import java.util.List;

public class AfterQuestionScorePageViewModel extends ViewModel implements LifecycleObserver {

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
}
