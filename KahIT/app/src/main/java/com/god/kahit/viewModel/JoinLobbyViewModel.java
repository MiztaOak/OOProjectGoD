package com.god.kahit.viewModel;

import android.content.Context;

import com.god.kahit.applicationEvents.RoomChangeEvent;
import com.god.kahit.Repository.Repository;
import com.god.kahit.networkManager.Connection;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

public class JoinLobbyViewModel extends ViewModel implements LifecycleObserver {
    private static final String TAG = JoinLobbyViewModel.class.getSimpleName();

    private MutableLiveData<List<Connection>> roomListForView;
    private Repository repository;

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

    public String getPlayerName() {
        return repository.getMyConnectionName();
    }

    public void setPlayerName(String newPlayerName) {
        repository.setMyConnectionName(newPlayerName);
    }
}
