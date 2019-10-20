package com.god.kahit.view;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.god.kahit.R;
import com.god.kahit.Repository.Repository;

/**
 * A class that takes care of app settings
 */
public class SettingsView extends AppCompatActivity {


    @SuppressLint("StaticFieldLeak")
    private Switch musicSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initMusicSwitch();

    }

    private void initMusicSwitch() {
        musicSwitch = new Switch(this);
        musicSwitch = findViewById(R.id.sLLmusicSwitch);
        SharedPreferences sharedpreferences = getSharedPreferences("save", MODE_PRIVATE); // to save the state of the switch
        switchCheckListener();
        musicSwitch.setChecked(sharedpreferences.getBoolean("value", true));  // giving a default state "on"

    }


    private void switchCheckListener() {
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (musicSwitch.isChecked()) {
                    // saving "on" state of the switch button when home button is clicked
                    saveSwitchStateWhenOn();
                    // turn on music
                    turnMusicOn();

                } else {
                    // saving "off" state of the switch button when home button is clicked
                    saveSwitchStateWhenOff();
                    //turn off music
                    turnMusicOff();

                }
            }
        });
    }

    private void saveSwitchStateWhenOff() {
        SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
        editor.putBoolean("value", false);
        editor.apply();
        musicSwitch.setChecked(false);
    }

    private void saveSwitchStateWhenOn() {
        SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
        editor.putBoolean("value", true);
        editor.apply();
        musicSwitch.setChecked(true);
    }

    private void turnMusicOff() {
        Repository.getInstance().pauseMusic();
        Repository.getInstance().setMusicState(false);
        Toast.makeText(getApplicationContext(), "Music off", Toast.LENGTH_SHORT).show();

    }

    private void turnMusicOn() {
        Repository.getInstance().resumeMusic();
        Repository.getInstance().setMusicState(true);
        Toast.makeText(getApplicationContext(), "Music on", Toast.LENGTH_SHORT).show();

    }


}

