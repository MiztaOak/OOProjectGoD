package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.god.kahit.R;
import com.god.kahit.model.Player;
import com.god.kahit.viewModel.HotSwapAddPlayersViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HotSwapAddPlayersView extends AppCompatActivity implements IOnPlayerClickListener {

    private static final String LOG_TAG = HotSwapAddPlayersView.class.getSimpleName();
    RecyclerView.LayoutManager layoutManager;
    MutableLiveData<List<Player>> playerListMutableLiveData;
    MutableLiveData<List<Integer>> integerListMutableLiveData;
    HotSwapAddPlayersViewModel hotSwapAddPlayersViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotswap_add_players);

        hotSwapAddPlayersViewModel = ViewModelProviders.of(this).get(HotSwapAddPlayersViewModel.class);
        getLifecycle().addObserver(hotSwapAddPlayersViewModel);
        hotSwapAddPlayersViewModel.onCreate();

        playerListMutableLiveData = hotSwapAddPlayersViewModel.getPlayerListForView();
        hotSwapAddPlayersViewModel.getPlayerListForView().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> playerList) {
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        integerListMutableLiveData = hotSwapAddPlayersViewModel.getTeamNumberForView();
        hotSwapAddPlayersViewModel.getTeamNumberForView().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
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
        recyclerView = (RecyclerView) findViewById(R.id.hsApPlayersRecyclerView);
        recyclerAdapter = new HotSwapRecyclerAdapter(this, playerListMutableLiveData, integerListMutableLiveData, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onPlayerClick(int position) {
        hotSwapAddPlayersViewModel.removePlayer(position);
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