package com.god.kahit.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.networkManager.Connection;
import com.god.kahit.viewModel.LobbyNetViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LobbyNetView extends AppCompatActivity implements IOnClickListener, AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = LobbyNetView.class.getSimpleName();
    private RecyclerView.LayoutManager layoutManager;
    private Spinner changeTeamSpinner;
    private RecyclerView recyclerView;
    private TextView sessionTypeTextView;
    private TextView roomNameTextView;
    private TextView gameModeTextView;
    private TextView nmbPlayersTextView;
    private Button startGameButton;

    private RecyclerView.Adapter recyclerAdapter;
    private LobbyNetViewModel lobbyNetViewModel;

    private MutableLiveData<List<Pair<Player, Connection>>> playerList;
    private MutableLiveData<List<Team>> teamList;
    private List<Integer> teamColors;
    private List<String> teamNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_net_activity);

        lobbyNetViewModel = ViewModelProviders.of(this).get(LobbyNetViewModel.class);

        determineIsHost();
        playerList = lobbyNetViewModel.getPlayerListForView();
        teamList = lobbyNetViewModel.getTeamListForView();

        lobbyNetViewModel.getPlayerListForView().observe(this, new Observer<List<Pair<Player, Connection>>>() { //todo remove?
            @Override
            public void onChanged(@Nullable List<Pair<Player, Connection>> integerStringMap) {
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        lobbyNetViewModel.getTeamListForView().observe(this, new Observer<List<Team>>() {
            @Override
            public void onChanged(@Nullable List<Team> teams) {
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        setupTextViews();
        setupRecyclerView();
        setupSpinner();

        lobbyNetViewModel.resetPlayerData();
        if(lobbyNetViewModel.isHost()) {
            lobbyNetViewModel.setupNewHostSession();
        }
    }

    private void determineIsHost() {
        Bundle b = getIntent().getExtras();

        boolean isHost = false;
        if (b != null) {
            isHost = b.getBoolean("isHostBoolean");
        }
        lobbyNetViewModel.setIsHost(isHost);
    }

    private void setupTextViews() {
        sessionTypeTextView = findViewById(R.id.lobbyNet_SessionType_textView);
        roomNameTextView = findViewById(R.id.lobbyNetRoomName_textView);
        gameModeTextView = findViewById(R.id.lobbyNet_GameMode_textView);
        nmbPlayersTextView = findViewById(R.id.lobbyNet_nmb_players_textView);
        startGameButton = findViewById(R.id.lobbyNetStartButton);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.lobbyNetTeamRecyclerView);
        recyclerAdapter = new LobbyNetRecyclerAdapter(this, teamList, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setupSpinner() {
        changeTeamSpinner = findViewById(R.id.spinnerChangeTeam);

        initTeamColors();
        initTeamNumbers();

        changeTeamSpinner.setAdapter(new CustomSpinnerAdapter(
                getApplicationContext(),
                R.layout.spinner_item,
                R.id.text1,
                teamNumbers,
                teamColors));

        changeTeamSpinner.setSelection(0);
        changeTeamSpinner.setBackgroundColor(teamColors.get(0));
        changeTeamSpinner.setOnItemSelectedListener(this);
    }

    private void initTeamNumbers() {
        teamNumbers = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            teamNumbers.add(" " + i + " ");
        }
    }

    private void initTeamColors() {
        teamColors = new ArrayList<>();
        int[] retrieve = getApplicationContext().getResources().getIntArray(R.array.androidcolors);
        for (int re : retrieve) {
            teamColors.add(re);
        }
    }

    private void handleChangeTeam(int teamIndex) {
        System.out.println("LobbyNetView - handleChangeTeam: Triggered!");
        changeTeamSpinner.setBackgroundColor(teamColors.get(teamIndex));
        if(teamList.getValue() != null) {
            lobbyNetViewModel.requestTeamChange(Objects.requireNonNull(Integer.toString(teamIndex)));
        }
    }

    @Override
    protected void onDestroy() {
        lobbyNetViewModel.resetPlayerData();
        super.onDestroy();
    }

    @Override
    public void onClick(Player player) {
        System.out.println("LobbyNetView - onClick: Player row delete button clicked!");
        //todo send request downwards to model -> repo -> (model || network)
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        handleChangeTeam(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("LobbyNetView - onNothingSelected: Triggered!");
    }
}

