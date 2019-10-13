package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.god.kahit.Events.GameLostConnectionEvent;
import com.god.kahit.Events.TeamChangeEvent;
import com.god.kahit.R;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.networkManager.Connection;
import com.god.kahit.viewModel.LobbyNetViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.god.kahit.model.QuizGame.BUS;

public class LobbyNetView extends AppCompatActivity implements IOnClickPlayerListener, AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = LobbyNetView.class.getSimpleName();
    private Spinner changeTeamSpinner;
    private RecyclerView recyclerView;
    private TextView sessionTypeTextView;
    private TextView roomNameTextView;
    private TextView gameModeTextView;
    private TextView nmbPlayersTextView;
    private Button startGameButton;

    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LobbyNetViewModel lobbyNetViewModel;

    private MutableLiveData<List<Pair<Player, Connection>>> playerList;
    private MutableLiveData<List<Team>> teamList;
    private MutableLiveData<String> myPlayerId;
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
        myPlayerId = lobbyNetViewModel.getMyPlayerId();

        playerList.observe(this, new Observer<List<Pair<Player, Connection>>>() { //todo remove?
            @Override
            public void onChanged(@Nullable List<Pair<Player, Connection>> integerStringMap) {
                recyclerAdapter.notifyDataSetChanged();
                updateTextAndButtonViews();
            }
        });

        teamList.observe(this, new Observer<List<Team>>() {
            @Override
            public void onChanged(@Nullable List<Team> teams) {
                recyclerAdapter.notifyDataSetChanged();
                updateTextAndButtonViews();
            }
        });

        myPlayerId.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        setupTextAndButtonViews();
        setupRecyclerView();
        setupSpinner();

        updateTextAndButtonViews();
        lobbyNetViewModel.resetPlayerData();
        lobbyNetViewModel.setupNewLobbySession(getApplicationContext());

        BUS.register(this);
    }

    private void determineIsHost() {
        Bundle b = getIntent().getExtras();

        boolean isHost = false;
        if (b != null) {
            isHost = b.getBoolean("isHostBoolean");
        }
        lobbyNetViewModel.setIsHost(isHost);
    }

    private void setupTextAndButtonViews() {
        sessionTypeTextView = findViewById(R.id.lobbyNet_SessionType_textView);
        roomNameTextView = findViewById(R.id.lobbyNetRoomName_textView);
        gameModeTextView = findViewById(R.id.lobbyNet_GameMode_textView);
        nmbPlayersTextView = findViewById(R.id.lobbyNet_nmb_players_textView);
        startGameButton = findViewById(R.id.lobbyNetStartButton);
    }

    private void updateTextAndButtonViews() {
        sessionTypeTextView.setText(String.format("%s - id: '%s'", lobbyNetViewModel.isHost() ? "Host" : "Client", myPlayerId.getValue()));

        gameModeTextView.setText(String.format("Game mode: %s", "Epic")); //todo use actual current gamemode

        int nmbPlayers = 0;
        if (playerList.getValue() != null) {
            nmbPlayers = playerList.getValue().size();
        }
        nmbPlayersTextView.setText(String.format("Players: %s/%s", nmbPlayers, "8")); //todo get max players from quizGame

        String buttonText;
        if (lobbyNetViewModel.isHost()) {
            buttonText = "Start game";
        } else {
            Pair<Player, Connection> myPlayerConnectionPair = lobbyNetViewModel.getMe();
            if (myPlayerConnectionPair != null && myPlayerConnectionPair.first.isPlayerReady()) {
                buttonText = "Unready";
            } else {
                buttonText = "Ready";
            }
        }

        startGameButton.setText(buttonText);
        if (lobbyNetViewModel.isHost()) {
            startGameButton.setEnabled(lobbyNetViewModel.areAllPlayersReady());
        }
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.lobbyNetTeamRecyclerView);
        recyclerAdapter = new LobbyNetRecyclerAdapter(this, teamList, playerList, myPlayerId, lobbyNetViewModel.isHost(), this);
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

        if (lobbyNetViewModel.isHost()) {
            changeTeamSpinner.setSelection(0);
            changeTeamSpinner.setBackgroundColor(teamColors.get(0));
        }
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
        lobbyNetViewModel.requestTeamChange(Integer.toString(teamIndex));
    }

    @Override
    protected void onDestroy() { //todo if player didnt start the game, and activity died, clearConnections too
        lobbyNetViewModel.stopHostBeacon();
        lobbyNetViewModel.resetPlayerData();
        super.onDestroy();
    }

    @Override
    public void onClick(Player player) {
        System.out.println("LobbyNetView - onClick: Player row delete button clicked!");
        lobbyNetViewModel.removePlayer(player);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        handleChangeTeam(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("LobbyNetView - onNothingSelected: Triggered!");
    }

    @Subscribe
    public void onGameLostConnectionEvent(GameLostConnectionEvent event) {
        if (!lobbyNetViewModel.isHost()) {
            Log.d(LOG_TAG, "onGameLostConnectionEvent: event triggered");
            lobbyNetViewModel.clearConnections();

            Toast.makeText(getApplicationContext(), "Lost connection to game!",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ChooseGameClass.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "onGameLostConnectionEvent: event triggered, but I am host - skipping");
        }
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        Team myTeam = lobbyNetViewModel.getMyTeam();
        if (myTeam != null) {
            int teamIdInt = Integer.valueOf(myTeam.getId());
            changeTeamSpinner.setBackgroundColor(teamColors.get(teamIdInt));
        } else {
            Log.d(LOG_TAG, "onTeamChangeEvent: myTeam == null, unable to update spinner background - skipping");
        }
    }
}

