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
import com.god.kahit.viewModel.HotSwapAddPlayerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

public class HotSwapAddPlayersClass extends AppCompatActivity {

    private static final String LOG_TAG = HotSwapAddPlayersClass.class.getSimpleName();
    MutableLiveData<Map<Integer, String>> playerMap;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotswap_add_players);

        HotSwapAddPlayerViewModel hotSwapAddPlayerViewModel = ViewModelProviders.of(this).get(HotSwapAddPlayerViewModel.class);
        playerMap = hotSwapAddPlayerViewModel.getPlayerMap();
        hotSwapAddPlayerViewModel.getPlayerMap().observe(this, new Observer<Map<Integer, String>>() {

            @Override
            public void onChanged(@Nullable Map<Integer, String> integerStringMap) {
                //TODO
            }

        });

        setupRecyclerView();

        Button addTeamButton = findViewById(R.id.addTeamButton);
        Button removeTeamButton = findViewById(R.id.removePlayerButton);

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOnePlayer();
            }
        });
        removeTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOnePlayer();
            }
        });
    }

    public void launchBackHotSwapGameModeClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, HotSwapGameModeClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchQuestionClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionClass.class);
        startActivity(intent);
    }

    /**
     * Sets up the RecyclerView with the helperClass RecyclerAdapter.
     */
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.hsApPlayersRecyclerView);
        recyclerAdapter = new RecyclerAdapter(this, playerMap);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Adds one player in the view and the ViewModel gets notified.
     */
    private void addOnePlayer() {
        int index = (recyclerAdapter.getItemCount());
        int insertIndex = index + 1;
        String stringValueOfInsertIndex = valueOf(insertIndex);
        String newPlayer = "Player " + stringValueOfInsertIndex;
        Map<Integer, String> map = playerMap.getValue();

        if (map != null && index < 9) {
            map.put(insertIndex, newPlayer);

            playerMap.setValue(map);
            recyclerAdapter.notifyItemInserted(insertIndex);
        }
    }

    /**
     * Removes one player in the view and the ViewModel gets notified.
     */
    private void removeOnePlayer() {
        int index = recyclerAdapter.getItemCount();
        if (index > 1 && playerMap.getValue() != null) {
            playerMap.getValue().remove(index);
            recyclerView.removeAllViews();
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    private List<Drawable> getDrawables() {
        List<Drawable> drawableList = new ArrayList<>();

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.player1);

        drawableList.add(drawable);

        return drawableList;
    }
}
