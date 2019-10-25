package com.god.kahit.view;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.god.kahit.R;
import com.god.kahit.viewModel.SettingsViewModel;

/**
 * responsibility: A class that is responsible of settings in the game
 * used-by: MainActivityView.
 *
 * @author Oussama Anadani
 */
public class SettingsView extends AppCompatActivity {

    SettingsViewModel settingsViewModel;

    @SuppressLint("StaticFieldLeak")
    private Switch musicSwitch; // using a switchButton(toggle) to turn on/off music

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        defineInstanceVariables();
        SharedPreferences sharedpreferences = getSharedPreferences("kahit", MODE_PRIVATE);  // getting the last state of the switchButton
        switchCheckListener();
        musicSwitch.setChecked(sharedpreferences.getBoolean("musicOn", true));  //set checked(on) as a default case for the switchButton
    }

    private void defineInstanceVariables() {
        musicSwitch = new Switch(this);
        musicSwitch = findViewById(R.id.sLLmusicSwitch);
    }

    /**
     * Switch listener method that does things depending of switchButton case
     */
    private void switchCheckListener() {
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (musicSwitch.isChecked()) {
                    //first, saving state of the switch button
                    saveSwitchButtonStateWhenOn();
                    // turn on music
                    turnMusicOn();

                } else {
                    //first, saving state of the switch button
                    saveSwitchButtonStateWhenOff();
                    //turn off music
                    turnMusicOff();
                }
            }
        });
    }

    private void saveSwitchButtonStateWhenOff() {
        SharedPreferences.Editor editor = getSharedPreferences("kahit", MODE_PRIVATE).edit();
        editor.putBoolean("musicOn", false);
        editor.apply();
        musicSwitch.setChecked(false);
    }

    private void saveSwitchButtonStateWhenOn() {
        SharedPreferences.Editor editor = getSharedPreferences("kahit", MODE_PRIVATE).edit();
        editor.putBoolean("musicOn", true);
        editor.apply();
        musicSwitch.setChecked(true);
    }

    private void turnMusicOff() {
        settingsViewModel.turnOffMusic();
        Toast.makeText(getApplicationContext(), "Music off", Toast.LENGTH_SHORT).show();

    }

    private void turnMusicOn() {
        settingsViewModel.turnOnMusic();
        Toast.makeText(getApplicationContext(), "Music on", Toast.LENGTH_SHORT).show();

    }


}

