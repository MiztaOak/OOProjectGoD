package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.god.kahit.R;
import com.god.kahit.Repository.Repository;
import com.god.kahit.model.GameMode;
import com.god.kahit.viewModel.HotSwapGameModeViewModel;

import java.util.ArrayList;
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
    private List<Button> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotswap_game_mode);

        hotSwapGameModeViewModel = ViewModelProviders.of(this).get(HotSwapGameModeViewModel.class);
        gameModes = hotSwapGameModeViewModel.getGameModes();
        initGameModeButtons();
    }

    private void initGameModeButtons() {
        buttons = new ArrayList<>();
        Button standard = findViewById(R.id.hsStandardButton);
        Button fast = findViewById(R.id.hsFastButton);
        Button hard = findViewById(R.id.hsHardButton);

        buttons.add(standard);
        buttons.add(fast);
        buttons.add(hard);

        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelected(v);
            }
        });

        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelected(v);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelected(v);
            }
        });
    }

    private void onSelected(View v) {
        for (Button button : buttons) {
            if (button == v) {
                button.setBackgroundResource(R.drawable.game_mode_button_selected);
            } else {
                button.setBackgroundResource(R.drawable.game_mode_button_normal);
            }
        }
    }

    public void launchHotSwapAddPlayerClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, HotSwapAddPlayersView.class);
        startActivity(intent);
    }

    public void launchBackChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Repository.getInstance().setupNewGameInstance(GameMode.HOT_SWAP);
    }
}
