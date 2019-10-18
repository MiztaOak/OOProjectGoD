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

import com.god.kahit.Repository.NameGenerator;
import com.god.kahit.R;
import com.god.kahit.viewModel.CreateLobbyNetViewModel;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class CreateLobbyNetView extends AppCompatActivity {

    private static final String LOG_TAG = CreateLobbyNetView.class.getSimpleName();
    private TextInputEditText playerNameTextInputEditText;
    private TextInputEditText roomNameTextInputEditText;
    private CreateLobbyNetViewModel createLobbyNetViewModel;

    private String prevPlayerName;
    private String prevRoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_lobbynet_activity);

        createLobbyNetViewModel = ViewModelProviders.of(this).get(CreateLobbyNetViewModel.class);
        createLobbyNetViewModel.setupNetwork(getApplicationContext());

        setupViewContent();
        updateViewContent();

        //Force set room & player name
        playerNameTextInputEditText.setText(NameGenerator.generatePlayerName());
        roomNameTextInputEditText.setText(NameGenerator.generateLobbyName());
        playerNameTextInputEditText.requestFocus();
        roomNameTextInputEditText.requestFocus();
        roomNameTextInputEditText.clearFocus();
    }

    private void setupViewContent() {
        playerNameTextInputEditText = findViewById(R.id.createLobbyNet_textInputEditText_playerName);
        roomNameTextInputEditText = findViewById(R.id.createLobbyNet_textInputEditText_roomName);

        setupTextInputDoneAction();

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
                        createLobbyNetViewModel.setPlayerName(inputString);
                    } else {
                        playerNameTextInputEditText.setText(prevPlayerName);
                    }
                } else {
                    prevPlayerName = editableText.toString();
                }
            }
        });

        roomNameTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Editable editableText = roomNameTextInputEditText.getText();
                if (editableText == null) {
                    return;
                }

                if (!hasFocus) {
                    String inputString = editableText.toString();
                    inputString = cleanInputString(inputString);

                    if (inputString != null) {
                        roomNameTextInputEditText.setText(inputString);
                        createLobbyNetViewModel.setRoomName(inputString);
                    } else {
                        roomNameTextInputEditText.setText(prevRoomName);
                    }
                } else {
                    prevRoomName = editableText.toString();
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

    private void setupTextInputDoneAction() {
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

        roomNameTextInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Force hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(playerNameTextInputEditText.getWindowToken(), 0);
                    roomNameTextInputEditText.clearFocus();
                }
                return false; //Hide keyboard, in case some devices don't beforehand
            }
        });
    }

    private void updateViewContent() {
        playerNameTextInputEditText.setText(createLobbyNetViewModel.getPlayerName());
        roomNameTextInputEditText.setText(createLobbyNetViewModel.getRoomName());
    }

    public void launchBackChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchLobbyNetActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");

        //Start new activity
        Intent intent = new Intent(this, LobbyNetView.class);
        intent.putExtra("isHostBoolean", true);
        startActivity(intent);
    }
}
