package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.god.kahit.R;
import com.god.kahit.backgroundMusicService.MusicService;

public class SettingsView extends AppCompatActivity {

    Switch musicSwitch;
    MainActivityView main = new MainActivityView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        musicSwitch = findViewById(R.id.sLLmusicSwitch);
        switchCheckListener();
    }


    private void switchCheckListener() {
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (musicSwitch.isChecked()) {
                    // turn music on
                   main.startMusic();
                    Toast.makeText(getApplicationContext(), "Music on", Toast.LENGTH_SHORT).show();
                } else {
                   main.onDestroy();
                    Toast.makeText(getApplicationContext(), "Music off", Toast.LENGTH_SHORT).show();
                    //turn music off
                }
            }
        });
    }




}

