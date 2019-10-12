package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.Events.GameJoinedLobbyEvent;
import com.god.kahit.Events.GameLostConnectionEvent;
import com.god.kahit.R;
import com.god.kahit.networkManager.Connection;
import com.god.kahit.viewModel.JoinRoomViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.god.kahit.model.QuizGame.BUS;

public class JoinRoomView extends AppCompatActivity {
    private static final String LOG_TAG = JoinRoomView.class.getSimpleName();
    private MutableLiveData<List<Connection>> roomList;
    private JoinRoomViewModel joinRoomViewModel;
    private RecyclerView recyclerView;

    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_room_activity);

        joinRoomViewModel = ViewModelProviders.of(this).get(JoinRoomViewModel.class);
        roomList = joinRoomViewModel.getListForView();
        roomList.observe(this, new Observer<List<Connection>>() {
            @Override
            public void onChanged(List<Connection> connections) {
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        setupRecyclerView();
        joinRoomViewModel.setupNetwork(getApplicationContext());
        joinRoomViewModel.startScan();

        BUS.register(this);
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.jChooseRoomRecyclerView);
        recyclerAdapter = new JoinRoomRecyclerAdapter(this, roomList, new IOnClickRoomListener() {
            @Override
            public void onClick(Connection roomConnection) {
                joinRoomViewModel.joinRoom(roomConnection);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void launchBackChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        joinRoomViewModel.stopScan();
        joinRoomViewModel.clearConnections();
        Intent intent = new Intent(this, ChooseGameClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchLobbyNetActivity(View view) { //otherwise remove whole method
        joinRoomViewModel.stopScan(); //todo change behaviour to select a server then press this?
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, LobbyNetView.class);
        intent.putExtra("isHostBoolean", false);
        startActivity(intent);
    }

    @Subscribe
    public void onLobbyJoinedEvent(GameJoinedLobbyEvent event) {
        Log.d(LOG_TAG, "onLobbyJoinedEvent: event triggered");
        joinRoomViewModel.stopScan();
        Intent intent = new Intent(this, LobbyNetView.class);
        intent.putExtra("isHostBoolean", false);
        startActivity(intent);
    }
}
