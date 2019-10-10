package com.god.kahit.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.god.kahit.R;
import com.god.kahit.model.Player;
import com.god.kahit.viewModel.TeamArrangementViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TeamArrangementView extends AppCompatActivity implements IOnClickListener, AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = TeamArrangementView.class.getSimpleName();
    RecyclerView.LayoutManager layoutManager;
    MutableLiveData<List<Pair<Player, Integer>>> playerList;
    TeamArrangementViewModel teamArrangementViewModel;
    List<Integer> teamColors;
    List<String> teamNumbers;
    private Spinner changeTeamSpinner;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_arrangement_activity);

        teamArrangementViewModel = ViewModelProviders.of(this).get(TeamArrangementViewModel.class);

        playerList = teamArrangementViewModel.getPlayerListForView();
        teamArrangementViewModel.getPlayerListForView().observe(this, new Observer<List<Pair<Player, Integer>>>() {

            @Override
            public void onChanged(@Nullable List<Pair<Player, Integer>> integerStringMap) {
                //recyclerView.removeAllViews();
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        setupRecyclerView();
        setupSpinner();
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.taPlayersRecyclerView);
        recyclerAdapter = new TeamArrangementRecyclerAdapter(this, playerList, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setupSpinner() {
        changeTeamSpinner = findViewById(R.id.spinnerChangeTeam);

        initTeamColors();
        initTeamNumbers();

        changeTeamSpinner.setAdapter(new CustomSpinnerAdapter(
                getApplicationContext(),
                R.layout.spinner_item,
                R.id.text1,
                teamNumbers,
                teamColors));

        changeTeamSpinner.setSelection(0);
        changeTeamSpinner.setBackgroundColor(teamColors.get(0));
        changeTeamSpinner.setOnItemSelectedListener(this);
    }

    private void initTeamNumbers() {
        teamNumbers = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            teamNumbers.add(" " + i + " ");
        }
    }

    private void initTeamColors() {
        teamColors = new ArrayList<>();
        int[] retrieve = getApplicationContext().getResources().getIntArray(R.array.androidcolors);
        for (int re : retrieve) {
            teamColors.add(re);
        }
    }

    private void handleChangeTeam(int teamIndex) {
        System.out.println("TeamArrangementView - handleChangeTeam: Triggered!");
        changeTeamSpinner.setBackgroundColor(teamColors.get(teamIndex));
    }

    @Override
    protected void onDestroy() {
        teamArrangementViewModel.resetPlayerData();
        super.onDestroy();
    }

    @Override
    public void onClick(int position) {
        System.out.println("TeamArrangementView - onClick: Player row delete button clicked!");
        //todo send request downwards to model -> repo -> (model || network)
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        handleChangeTeam(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("TeamArrangementView - onNothingSelected: Triggered!");
    }
}

