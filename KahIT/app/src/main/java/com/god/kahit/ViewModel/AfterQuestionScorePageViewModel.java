package com.god.kahit.ViewModel;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModel;

import com.god.kahit.Repository;
import com.god.kahit.model.Player;
import com.god.kahit.model.Tuple;

import java.util.ArrayList;
import java.util.List;

public class AfterQuestionScorePageViewModel extends ViewModel implements LifecycleObserver {

    public List<Tuple<String,String>> getScoreScreenContents(){
        List<Tuple<String,String>> tupleList = new ArrayList<>();
        for(Player player: Repository.getInstance().getPlayers()){
            Tuple<String, String> tuple = new Tuple<>(player.getName(),Integer.toString(player.getScore()));
            tupleList.add(tuple);
        }
        return tupleList;
    }

    public boolean isRoundOver(){
        return Repository.getInstance().isRoundOver();
    }
}
