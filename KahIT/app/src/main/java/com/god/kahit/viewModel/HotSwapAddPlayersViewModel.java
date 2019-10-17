package com.god.kahit.viewModel;

import android.util.Log;
import android.util.Pair;

import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class HotSwapAddPlayersViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = HotSwapAddPlayersViewModel.class.getSimpleName();

    private MutableLiveData<List<Pair<Player, Integer>>> playerListForView;

    public HotSwapAddPlayersViewModel() {
    }

    public MutableLiveData<List<Pair<Player, Integer>>> getPlayerListForView() {
        if (playerListForView == null) {
            playerListForView = new MutableLiveData<>();
            loadPlayerList();
        }
        return playerListForView;
    }

    public void loadPlayerList() {
        addNewPlayer();
        addNewPlayer();
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        List<Pair<Player, Integer>> sortedPlayerList = new ArrayList<>();

        String playerIdPrefix = "Player ";
        int playerSuffix = 1;

        while(sortedPlayerList.size() != Repository.getInstance().getPlayers().size()) {
            outerLabel:
            for (int i =0; i < event.getTeams().size(); i++) {
                for(Player player : event.getTeams().get(i).getTeamMembers()) {
                    if(player.getId().equals(playerIdPrefix + playerSuffix)) {
                        sortedPlayerList.add(new Pair<>(player, Integer.valueOf(event.getTeams().get(i).getId())));
                        break outerLabel;
                    }
                }
            }
            playerSuffix++;
        }
        playerListForView.setValue(sortedPlayerList);
    }

    public void addNewPlayer() {
        Repository.getInstance().addNewPlayerToEmptyTeam();
    }

    public void removePlayer(int position) {
        Repository.getInstance().removePlayer(Objects.requireNonNull(playerListForView.getValue()).get(position).first);
    }

    public void resetPlayerData() {
        Repository.getInstance().resetPlayerData();
    }

    public void onTeamChange(int position, int newTeamId) {
        Repository.getInstance().changeTeam(Objects.requireNonNull(playerListForView.getValue()).get(position).first, String.valueOf(newTeamId + 1));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        BUS.unregister(this);
    }

    @Override
    protected void onCleared() {
        Repository.getInstance().resetPlayerData();
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}
