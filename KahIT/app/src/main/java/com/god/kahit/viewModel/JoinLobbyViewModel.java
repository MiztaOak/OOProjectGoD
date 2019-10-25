package com.god.kahit.viewModel;

import android.content.Context;

import com.god.kahit.applicationEvents.RoomChangeEvent;
import com.god.kahit.model.GameMode;
import com.god.kahit.networkManager.Connection;
import com.god.kahit.repository.NameGenerator;
import com.god.kahit.repository.Repository;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * responsibility: ViewModel for the JoinLobbyView.
 * Handles the clients request to join a lobby and calls the repository to instantiate a new Player.
 * <p>
 * used-by: LobbyNetView.
 *
 * @author Mats Cedervall
 */
public class JoinLobbyViewModel extends ViewModel implements LifecycleObserver {
    private static final String TAG = JoinLobbyViewModel.class.getSimpleName();
    private final Repository repository;
    private MutableLiveData<List<Connection>> roomListForView;

    public JoinLobbyViewModel() {
        repository = Repository.getInstance();
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

    @Subscribe
    public void onRoomChangeEvent(RoomChangeEvent event) {
        roomListForView.setValue(event.getRoomsConnection());
    }

    public MutableLiveData<List<Connection>> getListForView() {
        if (roomListForView == null) {
            roomListForView = new MutableLiveData<>();
        }
        return roomListForView;
    }

    public void setupNetwork(Context context) {
        repository.setupNetwork(context, false);
    }

    public void joinRoom(Connection roomConnection) {
        repository.joinRoom(roomConnection);
    }

    public void startScan() {
        repository.startScan();
    }

    public void stopScan() {
        repository.stopScan();
    }

    public void clearConnections() {
        repository.clearConnections();
    }

    public String getNewGeneratedPlayerName() {
        return NameGenerator.generatePlayerName();
    }

    public String getPlayerName() {
        return repository.getMyConnectionName();
    }

    public void setPlayerName(String newPlayerName) {
        repository.setMyConnectionName(newPlayerName);
    }

    public void setupNewGameInstance() {
        Repository.getInstance().setupNewGameInstance(GameMode.CLIENT);
    }
}
