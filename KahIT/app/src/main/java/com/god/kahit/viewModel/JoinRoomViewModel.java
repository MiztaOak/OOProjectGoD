package com.god.kahit.viewModel;

import android.content.Context;

import com.god.kahit.Events.RoomChangeEvent;
import com.god.kahit.Repository;
import com.god.kahit.networkManager.Connection;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;


public class JoinRoomViewModel extends ViewModel implements LifecycleObserver {
    private static final String TAG = JoinRoomViewModel.class.getSimpleName();

    MutableLiveData<List<Connection>> roomListForView;
    private Repository repository;

    public JoinRoomViewModel() {
        repository = Repository.getInstance();
        BUS.register(this);
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
}
