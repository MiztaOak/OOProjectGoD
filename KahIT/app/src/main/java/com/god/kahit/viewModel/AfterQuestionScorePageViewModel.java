package com.god.kahit.viewModel;


import android.util.Pair;

import com.god.kahit.Repository;
import com.god.kahit.model.Player;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

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
