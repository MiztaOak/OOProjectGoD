package com.god.kahit.view;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.god.kahit.R;
import com.god.kahit.Repository.Repository;

public class SettingsView extends AppCompatActivity {
    private Switch musicSwitch;


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
                    // turn on music
                    Repository.getInstance().resumeMusic();
                    Toast.makeText(getApplicationContext(), "Music on", Toast.LENGTH_SHORT).show();
                } else {
                    //turn off music
                    Repository.getInstance().pauseMusic();
                    Toast.makeText(getApplicationContext(), "Music off", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}

