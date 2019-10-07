package com.god.kahit.view;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.god.kahit.R;
import com.god.kahit.ViewModel.TeamArrangementViewModel;
import com.god.kahit.model.Player;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamArrangementView extends AppCompatActivity implements HotSwapRecyclerAdapter.IOnPlayerClickListener {

    private static final String LOG_TAG = TeamArrangementView.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    MutableLiveData<List<Player>> playerList;
    TeamArrangementViewModel teamArrangementViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_arrangement_activity);

        teamArrangementViewModel = ViewModelProviders.of(this).get(TeamArrangementViewModel.class);

        setupRecyclerView();
        playerList = teamArrangementViewModel.getPlayerList();
        teamArrangementViewModel.getPlayerList().observe(this, new Observer<List<Player>>() {

            @Override
            public void onChanged(@Nullable List<Player> integerStringMap) {
                recyclerView.removeAllViews();
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        setupRecyclerView();

        Button addTeamButton = findViewById(R.id.addTeamButton);
        Button removeTeamButton = findViewById(R.id.removePlayerButton);

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamArrangementViewModel.addNewPlayer();
            }
        });
        removeTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamArrangementViewModel.removeOnePlayer();
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.hsApPlayersRecyclerView);
        recyclerAdapter = new HotSwapRecyclerAdapter(this, playerList, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onDestroy() {
        teamArrangementViewModel.resetPlayerData();
        super.onDestroy();
    }

    @Override
    public void onPlayerClick(int position) {

    }

    @Override
    public void onTeamSelected(int position, int teamId) {

    }
}

