package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;
import com.god.kahit.ViewModel.HotSwapGameModeViewModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class HotSwapGameModeView extends AppCompatActivity {

    private static final String LOG_TAG = HotSwapGameModeView.class.getSimpleName();

    HotSwapGameModeViewModel hotSwapGameModeViewModel;

    private MutableLiveData<List<String>> gameModes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotswap_game_mode);

        hotSwapGameModeViewModel = ViewModelProviders.of(this).get(HotSwapGameModeViewModel.class);
        gameModes = hotSwapGameModeViewModel.getGameModes();
        hotSwapGameModeViewModel.getGameModes().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {

            }
        });
    }

    public void launchHotSwapAddPlayerClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, HotSwapAddPlayersView.class);
        startActivity(intent);
    }

    public void launchBackChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
