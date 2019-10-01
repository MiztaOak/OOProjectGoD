package com.god.kahit.view;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.god.kahit.R;

import com.god.kahit.ViewModel.HotSwapAddPlayersViewModel;


import java.util.ArrayList;
import java.util.List;

public class HotSwapAddPlayersView extends AppCompatActivity {

    private static final String LOG_TAG = HotSwapAddPlayersView.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    MutableLiveData<List<String>> playerMap;
    HotSwapAddPlayersViewModel hotSwapAddPlayersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotswap_add_players);

        hotSwapAddPlayersViewModel = ViewModelProviders.of(this).get(HotSwapAddPlayersViewModel.class);
        playerMap = hotSwapAddPlayersViewModel.getPlayerMap();
        hotSwapAddPlayersViewModel.getPlayerMap().observe(this, new Observer<List<String>>() {

            @Override
            public void onChanged(@Nullable List<String> integerStringMap) {
                recyclerView.removeAllViews();
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        setupRecyclerView();

        Button addTeamButton = findViewById(R.id.addTeamButton);
        Button removeTeamButton = findViewById(R.id.removePlayerButton);

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotSwapAddPlayersViewModel.addOnePlayer();
            }
        });
        removeTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotSwapAddPlayersViewModel.removeOnePlayer();
            }
        });
    }

    public void launchBackHotSwapGameModeClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, HotSwapGameModeView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchQuestionClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionClass.class);
        startActivity(intent);
    }

    /**
     * Sets up the recyclerView with it's adaptor HotSwapRecyclerAdapter.
     */
    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.hsApPlayersRecyclerView);
        recyclerAdapter = new HotSwapRecyclerAdapter(this, playerMap);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public List<Drawable> getDrawables() { //TODO this is supposed to be a method for loading in the player-drawables since we can't do that in viewModel.
        List<Drawable> drawableList = new ArrayList<>();

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.player1);

        drawableList.add(drawable);


        return drawableList;
    }
}
