package com.god.kahit.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.god.kahit.R;
import com.god.kahit.applicationEvents.GameJoinedLobbyEvent;
import com.god.kahit.networkManager.Connection;
import com.god.kahit.viewModel.JoinLobbyViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.god.kahit.applicationEvents.EventBusGreenRobot.BUS;

/**
 * responsibility: Class is responsible for the Join View in the multiplayer lobby.
 * used-by: AfterQuestionScorePageView, ChooseGameView.
 *
 * @author Mats Cedervall
 */
public class JoinLobbyNetView extends AppCompatActivity {
    private static final String LOG_TAG = JoinLobbyNetView.class.getSimpleName();
    private MutableLiveData<List<Connection>> roomList;
    private JoinLobbyViewModel joinLobbyViewModel;

    private TextInputEditText playerNameTextInputEditText;
    private RecyclerView.Adapter recyclerAdapter;

    private String prevPlayerName;

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

        setupViewContent();
        setupRecyclerView();
        joinLobbyViewModel.setupNewGameInstance();
        joinLobbyViewModel.setupNetwork(getApplicationContext());

        //Force set player name
        playerNameTextInputEditText.setText(joinLobbyViewModel.getNewGeneratedPlayerName());
        playerNameTextInputEditText.requestFocus();
        playerNameTextInputEditText.clearFocus();

        joinLobbyViewModel.startScan();
    }

    @Override
    public void onStart() {
        super.onStart();
        joinLobbyViewModel.setupNewGameInstance();
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BUS.unregister(this);
    }

    private void setupViewContent() {
        playerNameTextInputEditText = findViewById(R.id.createLobbyNet_textInputEditText_playerName);

        playerNameTextInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Force hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(playerNameTextInputEditText.getWindowToken(), 0);
                    playerNameTextInputEditText.clearFocus();
                }
                return false; //Hide keyboard, in case some devices don't beforehand
            }
        });

        playerNameTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Editable editableText = playerNameTextInputEditText.getText();
                if (editableText == null) {
                    return;
                }

                if (!hasFocus) {
                    String inputString = editableText.toString();
                    inputString = cleanInputString(inputString);

                    if (inputString != null) {
                        playerNameTextInputEditText.setText(inputString);
                        joinLobbyViewModel.setPlayerName(inputString);
                    } else {
                        playerNameTextInputEditText.setText(prevPlayerName);
                    }
                } else {
                    prevPlayerName = editableText.toString();
                }
            }
        });
    }

    private String cleanInputString(String inputText) {
        inputText = inputText.replace(";", "");
        inputText = inputText.trim(); //Remove leading and trailing spaces

        if (inputText.length() > 0) {
            return inputText;
        } else {
            return null;
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.jChooseRoomRecyclerView);
        recyclerAdapter = new JoinLobbyNetRecyclerAdapter(roomList, new IOnClickLobbyListener() {
            @Override
            public void onClick(Connection roomConnection) {
                joinLobbyViewModel.joinRoom(roomConnection);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
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
        joinLobbyViewModel.stopScan();
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
