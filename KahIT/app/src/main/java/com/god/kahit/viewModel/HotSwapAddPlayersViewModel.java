package com.god.kahit.viewModel;

import android.util.Pair;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.god.kahit.repository.Repository;
import com.god.kahit.model.Player;
import com.god.kahit.model.modelEvents.TeamChangeEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * responsibility: The viewModel for the HotSwapAddPlayersView
 * Fetches and handles the data needed for the view such as teams and players.
 * <p>
 * used-by: HotSwapAddPlayersView
 *
 * @author Jakob Ewerstrand
 */
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

    /**
     * Prepares the layerList by calling addNewPlayer twice.
     */
    private void loadPlayerList() {
        addNewPlayer();
        addNewPlayer();
    }

    /**
     * Method that receives a TeamChangeEvent and sorts through it placing the players in oder by playerId.
     * When a player is found it matches the player and the team number i in a Pair.
     * When all players are sorted it sets the value in the mutableLiveData.
     *
     * @param event - event containing Teams which in turn hold players.
     */
    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        List<Pair<Player, Integer>> sortedPlayerList = new ArrayList<>();

        String playerIdPrefix = "Player ";
        int playerSuffix = 1;

        while (sortedPlayerList.size() != Repository.getInstance().getPlayers().size()) {
            outerLabel:
            for (int i = 0; i < event.getTeams().size(); i++) {
                for (Player player : event.getTeams().get(i).getTeamMembers()) {
                    if (player.getId().equals(playerIdPrefix + playerSuffix)) {
                        sortedPlayerList.add(new Pair<>(player, Integer.valueOf(event.getTeams().get(i).getId())));
                        break outerLabel;
                    }
                }
            }
            playerSuffix++;
        }
        playerListForView.setValue(sortedPlayerList);
    }

    /**
     * Adds a new player by calling repository.
     */
    public void addNewPlayer() {
        Repository.getInstance().addNewPlayerToEmptyTeam();
    }

    /**
     * Removes a specific player by calling the repository
     *
     * @param position - index of the player to be removed.
     */
    public void removePlayer(int position) {
        Repository.getInstance().removePlayer(Objects.requireNonNull(playerListForView.getValue()).get(position).first);
    }

    /**
     * Method that changes the current team of a player by calling the repository.
     *
     * @param position  -index of the player that wants to change team.
     * @param newTeamId -index of the new team.
     */
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
}
