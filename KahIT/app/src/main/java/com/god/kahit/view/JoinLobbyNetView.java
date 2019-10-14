package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.Events.GameJoinedLobbyEvent;
import com.god.kahit.R;
import com.god.kahit.networkManager.Connection;
import com.god.kahit.viewModel.JoinLobbyViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.god.kahit.model.QuizGame.BUS;

public class JoinLobbyNetView extends AppCompatActivity {
    private static final String LOG_TAG = JoinLobbyNetView.class.getSimpleName();
    private MutableLiveData<List<Connection>> roomList;
    private JoinLobbyViewModel joinLobbyViewModel;
    private RecyclerView recyclerView;

    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_lobbynet_activity);

        joinLobbyViewModel = ViewModelProviders.of(this).get(JoinLobbyViewModel.class);
        getLifecycle().addObserver(joinLobbyViewModel);
        joinLobbyViewModel.onCreate();
        BUS.register(this);

        roomList = joinLobbyViewModel.getListForView();
        roomList.observe(this, new Observer<List<Connection>>() {
            @Override
            public void onChanged(List<Connection> connections) {
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        setupRecyclerView();
        joinLobbyViewModel.setupNetwork(getApplicationContext());
        joinLobbyViewModel.startScan();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BUS.unregister(this);
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.jChooseRoomRecyclerView);
        recyclerAdapter = new JoinLobbyNetRecyclerAdapter(roomList, new IOnClickLobbyListener() {
            @Override
            public void onClick(Connection roomConnection) {
                joinLobbyViewModel.joinRoom(roomConnection);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void launchBackChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        joinLobbyViewModel.stopScan();
        joinLobbyViewModel.clearConnections();
        Intent intent = new Intent(this, ChooseGameClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchLobbyNetActivity(View view) { //otherwise remove whole method
        joinLobbyViewModel.stopScan(); //todo change behaviour to select a server then press this?
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, LobbyNetView.class);
        intent.putExtra("isHostBoolean", false);
        startActivity(intent);
    }

    @Subscribe
    public void onLobbyJoinedEvent(GameJoinedLobbyEvent event) {
        Log.d(LOG_TAG, "onLobbyJoinedEvent: event triggered");
        joinLobbyViewModel.stopScan();
        Intent intent = new Intent(this, LobbyNetView.class);
        intent.putExtra("isHostBoolean", false);
        startActivity(intent);
        finish();
    }
}
