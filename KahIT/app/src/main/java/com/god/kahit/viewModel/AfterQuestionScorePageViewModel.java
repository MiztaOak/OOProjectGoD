package com.god.kahit.viewModel;


import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

import com.god.kahit.repository.Repository;
import com.god.kahit.applicationEvents.NewViewEvent;
import com.god.kahit.model.Player;
import com.god.kahit.view.CategoryView;
import com.god.kahit.view.QuestionView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * The viewModel for the AfterQuestionScorePageView, the class handles the fetching of data for the
 * view.
 * <p>
 * used by: AfterQuestionScorePageView
 *
 * @author Johan EK
 */
public class AfterQuestionScorePageViewModel extends ViewModel implements LifecycleObserver {
    private static final String LOG_TAG = AfterQuestionScorePageViewModel.class.getSimpleName();
    private Repository repository;

    public AfterQuestionScorePageViewModel() {
        repository = Repository.getInstance();
    }

    public List<Player> getScoreScreenContents() {
        List<Player> playerList = new ArrayList<>();
        playerList.addAll(repository.getPlayers());

        sortPlayerList(playerList);

        return playerList;
    }

    public String getMyPlayerId() {
        return repository.getCurrentPlayer().getId();
    }

    private void sortPlayerList(List<Player> playerList) {
        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.getScore() - o1.getScore();
            }
        });
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

        if (isRoundOver()) {
            newViewClass = CategoryView.class;
        } else {
            newViewClass = QuestionView.class;
        }

        repository.broadcastShowNewView(newViewClass);
        BUS.post(new NewViewEvent(newViewClass));
    }
}
