package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.god.kahit.R;
import com.god.kahit.ViewModel.HotSwapAddPlayersViewModel;
import com.god.kahit.model.Player;

import java.util.List;

public class HotSwapAddPlayersView extends AppCompatActivity implements IOnPlayerClickListener {

    private static final String LOG_TAG = HotSwapAddPlayersView.class.getSimpleName();
    RecyclerView.LayoutManager layoutManager;
    MutableLiveData<List<Pair<Player, Integer>>> playerMap;
    HotSwapAddPlayersViewModel hotSwapAddPlayersViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotswap_add_players);

        hotSwapAddPlayersViewModel = ViewModelProviders.of(this).get(HotSwapAddPlayersViewModel.class);
        playerMap = hotSwapAddPlayersViewModel.getListForView();
        hotSwapAddPlayersViewModel.getListForView().observe(this, new Observer<List<Pair<Player, Integer>>>() {

            @Override
            public void onChanged(@Nullable List<Pair<Player, Integer>> integerStringMap) {
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        setupRecyclerView();

        Button addTeamButton = findViewById(R.id.addTeamButton);

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotSwapAddPlayersViewModel.addNewPlayer();
            }
        });
    }

    /**
     * Sets up the recyclerView with it's adaptor HotSwapRecyclerAdapter.
     */
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.hsApPlayersRecyclerView);
        recyclerAdapter = new HotSwapRecyclerAdapter(this, playerMap, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onPlayerClick(int position) {
        hotSwapAddPlayersViewModel.removePlayer(playerMap.getValue().get(position).first);
    }

    @Override
    public void onTeamSelected(int position, int newTeamId) {
        hotSwapAddPlayersViewModel.updatePlayerData(position, newTeamId);
    }

    @Override
    protected void onDestroy() {
        hotSwapAddPlayersViewModel.resetPlayerData();
        super.onDestroy();
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
}