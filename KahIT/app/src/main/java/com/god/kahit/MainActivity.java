package com.god.kahit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.god.kahit.controller.ChooseGameClass;

public class MainActivity extends AppCompatActivity {
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        startGameButton = findViewById(R.id.mPlayButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                new ChooseGameClass();
            }
        });
    }
}
