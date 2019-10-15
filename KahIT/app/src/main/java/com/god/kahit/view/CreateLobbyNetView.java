package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;
import com.god.kahit.viewModel.CreateLobbyNetViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

public class CreateLobbyNetView extends AppCompatActivity {

    private static final String LOG_TAG = CreateLobbyNetView.class.getSimpleName();

    MutableLiveData<List<String>> list;
    CreateLobbyNetViewModel createLobbyNetViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_lobbynet_activity);

        createLobbyNetViewModel = ViewModelProviders.of(this).get(CreateLobbyNetViewModel.class);
        list = createLobbyNetViewModel.getListForView();
        createLobbyNetViewModel.getListForView().observe(this, new Observer<List<String>>() {

            @Override
            public void onChanged(@Nullable List<String> integerStringMap) {
            }
        });
    }

    public void launchBackChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchQuestionClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionView.class);
        startActivity(intent);
    }

    public void launchLobbyNetActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, LobbyNetView.class);
        intent.putExtra("isHostBoolean", true);
        startActivity(intent);
    }
}
