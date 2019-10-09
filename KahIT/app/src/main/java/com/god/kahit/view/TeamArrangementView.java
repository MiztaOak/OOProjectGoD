package com.god.kahit.view;

import android.os.Bundle;

import com.god.kahit.R;
import com.god.kahit.viewModel.TeamArrangementViewModel;
import com.god.kahit.model.Player;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamArrangementView extends AppCompatActivity implements IOnPlayerClickListener {

    private static final String LOG_TAG = TeamArrangementView.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    MutableLiveData<List<Player>> playerList;
    TeamArrangementViewModel teamArrangementViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_arrangement_activity);

        teamArrangementViewModel = ViewModelProviders.of(this).get(TeamArrangementViewModel.class);

        //playerList = teamArrangementViewModel.getListForView();
       /* teamArrangementViewModel.getListForView().observe(this, new Observer<List<Player>>() {

            @Override
            public void onChanged(@Nullable List<Player> integerStringMap) {
                //recyclerView.removeAllViews();
                recyclerAdapter.notifyDataSetChanged();
            }
        });*/

        setupRecyclerView();

    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.taPlayersRecyclerView);
        recyclerAdapter = new TeamArrangementRecyclerAdapter(this, playerList, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onDestroy() {
        teamArrangementViewModel.resetPlayerData();
        super.onDestroy();
    }

    @Override
    public void onPlayerClick(int position) {

    }

    @Override
    public void onTeamSelected(int position, int teamId) {

    }
}

