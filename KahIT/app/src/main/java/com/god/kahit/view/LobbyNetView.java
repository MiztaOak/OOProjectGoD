package com.god.kahit.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.god.kahit.R;
import com.god.kahit.applicationEvents.GameLostConnectionEvent;
import com.god.kahit.applicationEvents.GameStartedEvent;
import com.god.kahit.applicationEvents.TimedOutEvent;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.model.modelEvents.TeamChangeEvent;
import com.god.kahit.networkManager.Connection;
import com.god.kahit.viewModel.LobbyNetViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * responsibility: Class that is responsible for the multiplayer lobby.
 * used-by: AfterQuestionScorePageView, ChooseGameView, Repository.
 *
 * @author Oussama Anadani, Jakob Ewerstrand
 */
public class LobbyNetView extends AppCompatActivity implements IOnClickPlayerListener, AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = LobbyNetView.class.getSimpleName();
    private static final String START_GAME_BUTTON_COLOR = "#00CBF8";
    private static final String START_GAME_BUTTON_COLOR_DISABLED = "#6A8990";
    private Spinner changeTeamSpinner;
    private RecyclerView recyclerView;
    private TextView sessionTypeTextView;
    private TextView lobbyNameTextView;
    private TextView gameModeTextView;
    private TextView nmbPlayersTextView;
    private Button startGameButton;

    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LobbyNetViewModel lobbyNetViewModel;

    private MutableLiveData<List<Pair<Player, Connection>>> playerList;
    private MutableLiveData<List<Team>> teamList;
    private MutableLiveData<String> myPlayerId;
    private MutableLiveData<String> lobbyName;
    private List<Integer> teamColors;
    private List<String> teamNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_net_activity);

        lobbyNetViewModel = ViewModelProviders.of(this).get(LobbyNetViewModel.class);
        getLifecycle().addObserver(lobbyNetViewModel);
        lobbyNetViewModel.onCreate();

        determineIsHost();
        playerList = lobbyNetViewModel.getPlayerListForView();
        teamList = lobbyNetViewModel.getTeamListForView();
        myPlayerId = lobbyNetViewModel.getMyPlayerId();
        lobbyName = lobbyNetViewModel.getLobbyName();

        playerList.observe(this, new Observer<List<Pair<Player, Connection>>>() { //todo remove?
            @Override
            public void onChanged(@Nullable List<Pair<Player, Connection>> integerStringMap) {
                recyclerAdapter.notifyDataSetChanged();
                updateViewContent();
            }
        });

        teamList.observe(this, new Observer<List<Team>>() {
            @Override
            public void onChanged(@Nullable List<Team> teams) {
                recyclerAdapter.notifyDataSetChanged();
                updateViewContent();
            }
        });

        myPlayerId.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recyclerAdapter.notifyDataSetChanged();
                updateViewContent();
            }
        });

        lobbyName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                updateViewContent();
            }
        });


        setupTextAndButtonViews();
        setupRecyclerView();
        setupSpinner();

        updateViewContent();

        BUS.register(this);

        if (lobbyNetViewModel.isHost()) {
            lobbyNetViewModel.fireTeamChangeEvent(); //Update list with host player
            lobbyNetViewModel.startHostBeacon();
        } else {
            //Restore net in communication as all logic is setup
            lobbyNetViewModel.restoreNetInCommunication();
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

    private void setupTextAndButtonViews() {
        sessionTypeTextView = findViewById(R.id.lobbyNet_SessionType_textView);
        lobbyNameTextView = findViewById(R.id.lobbyNetRoomName_textView);
        gameModeTextView = findViewById(R.id.lobbyNet_GameMode_textView);
        nmbPlayersTextView = findViewById(R.id.lobbyNet_nmb_players_textView);
        startGameButton = findViewById(R.id.lobbyNetStartButton);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartGameButtonAction();
            }
        });
    }

    private void updateViewContent() {
        sessionTypeTextView.setText(String.format("%s - id: '%s'", lobbyNetViewModel.isHost() ? "Host" : "Client", myPlayerId.getValue()));

        if (lobbyName.getValue() != null) {
            lobbyNameTextView.setText(lobbyName.getValue());
        } else {
            lobbyNameTextView.setText("Default name");
        }

        gameModeTextView.setText(String.format("Standard")); //todo use actual current gamemode

        int nmbPlayers = 0;
        if (playerList.getValue() != null) {
            nmbPlayers = playerList.getValue().size();
        }
        nmbPlayersTextView.setText(String.format("Players: %s/%s", nmbPlayers, "8")); //todo get max players from quizGame

        String readyButtonText;
        if (lobbyNetViewModel.isHost()) {
            readyButtonText = "Start game";
            if (lobbyNetViewModel.areAllPlayersReady()) {
                startGameButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(START_GAME_BUTTON_COLOR)));
                startGameButton.setEnabled(true);
            } else {
                startGameButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(START_GAME_BUTTON_COLOR_DISABLED)));
                startGameButton.setEnabled(false);
            }
        } else {
            Pair<Player, Connection> myPlayerConnectionPair = lobbyNetViewModel.getMe();
            Team myTeam = lobbyNetViewModel.getMyTeam();

            if (myPlayerConnectionPair == null || myTeam == null) {
                readyButtonText = "Ready";
                changeTeamSpinner.setEnabled(true);
            } else {
                if (myPlayerConnectionPair.first.isReady()) {
                    readyButtonText = "Unready";
                    changeTeamSpinner.setBackgroundColor(0xAAAAAAAA);
                    changeTeamSpinner.setEnabled(false);
                } else {
                    readyButtonText = "Ready";
                    changeTeamSpinner.setBackgroundColor(teamColors.get(Integer.valueOf(myTeam.getId()) - 1));
                    changeTeamSpinner.setEnabled(true);
                }
            }
        }

        startGameButton.setText(readyButtonText);
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

        changeTeamSpinner.setSelection(0, false); //Fix bug with an otherwise triggered event as soon as animation is complete, messing things up
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
        Log.d(LOG_TAG, "handleChangeTeam: Triggered!");
        changeTeamSpinner.setBackgroundColor(teamColors.get(teamIndex));
        lobbyNetViewModel.requestTeamChange(Integer.toString(teamIndex + 1));
    }

    private void doSilentSpinnerUpdate(int teamIdInt) {
        changeTeamSpinner.setOnItemSelectedListener(null);
        changeTeamSpinner.setSelection(teamIdInt - 1, false);
        changeTeamSpinner.setOnItemSelectedListener(this);
        updateViewContent();
    }

    private void onStartGameButtonAction() {
        if (lobbyNetViewModel.isHost()) {
            Log.d(LOG_TAG, "onStartGameButtonAction: start game action called");
            if (lobbyNetViewModel.isHost()) {
                lobbyNetViewModel.startGame();
                Intent intent = new Intent(this, PreGameCountdownView.class);
                startActivity(intent);
                finish();
            }

        } else {
            if (startGameButton.getText().equals("Ready")) {
                Log.d(LOG_TAG, "onStartGameButtonAction: player ready action called");
                lobbyNetViewModel.requestSetReady(true);
            } else if (startGameButton.getText().equals("Unready")) {
                Log.d(LOG_TAG, "onStartGameButtonAction: player unready action called");
                lobbyNetViewModel.requestSetReady(false);
            } else {
                Log.e(LOG_TAG, "onStartGameButtonAction: unknown startGameButton state, ignoring button tap");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        lobbyNetViewModel.stopHostBeacon();
    }

    @Override
    protected void onDestroy() {
        BUS.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(Player player) {
        Log.d(LOG_TAG, "onClick: Player kick button clicked!");
        lobbyNetViewModel.removePlayer(player);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        handleChangeTeam(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(LOG_TAG, "onNothingSelected: Triggered!");
    }

    @Subscribe
    public void onGameLostConnectionEvent(GameLostConnectionEvent event) {
        if (!lobbyNetViewModel.isHost()) {
            Log.d(LOG_TAG, "onGameLostConnectionEvent: event triggered");
            lobbyNetViewModel.clearConnections();

            Toast.makeText(getApplicationContext(), "Lost connection to game!",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ChooseGameView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Log.d(LOG_TAG, "onGameLostConnectionEvent: event triggered, but I am host - skipping");
        }
    }

    @Subscribe
    public void onTeamChangeEvent(TeamChangeEvent event) {
        Team myTeam = lobbyNetViewModel.getMyTeam();
        if (myTeam != null) {
            int teamIdInt = Integer.valueOf(myTeam.getId());
            doSilentSpinnerUpdate(teamIdInt);
        } else {
            Log.d(LOG_TAG, "onTeamChangeEvent: myTeam == null, unable to update spinner background - skipping");
        }
    }

    @Subscribe
    public void onTimedOut(TimedOutEvent event) {
        if (lobbyNetViewModel.isHost()) { //Client is handled through onGameLostConnectionEvent
            Intent intent = new Intent(this, ChooseGameView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Subscribe
    public void onGameStart(GameStartedEvent event) {
        Intent intent = new Intent(this, PreGameCountdownView.class);
        startActivity(intent);
        finish();
    }
}

