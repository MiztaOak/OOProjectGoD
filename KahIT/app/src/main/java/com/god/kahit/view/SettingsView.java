package com.god.kahit.view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.god.kahit.R;

public class SettingsView extends AppCompatActivity {
    MediaPlayer m1 = null;
    Switch musicSwitch;

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
                    stopPlaying();
                    m1 = MediaPlayer.create(SettingsView.this, R.raw.pre_game_song);
                    m1.start();
                    Toast.makeText(getApplicationContext(), "Music on", Toast.LENGTH_SHORT).show();
                } else {
                    stopPlaying();
                    Toast.makeText(getApplicationContext(), "Music off", Toast.LENGTH_SHORT).show();
                    //turn music off
                }
            }
        });
    }

    private void stopPlaying() {
        if (m1 != null) {
            m1.stop();
            m1.release();
            m1 = null;
        }


    }
}
