package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.god.kahit.R;
import com.god.kahit.model.Player;
import com.god.kahit.viewModel.HotSwapAddPlayersViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * responsibility: Class responsible for the HotSwap "lobby" view.
 * Along with it's helper classes it displays the players name, picture and team in a row format.
 * <p>
 * used-by: HotSwapGameModeView.
 *
 * @author Jakob Ewerstrand
 */
public class HotSwapAddPlayersView extends AppCompatActivity implements IHotSwapViewHolderClickListener {

    private static final String LOG_TAG = HotSwapAddPlayersView.class.getSimpleName();

    MutableLiveData<List<Pair<Player, Integer>>> playerListMutableLiveData;

    HotSwapAddPlayersViewModel hotSwapAddPlayersViewModel;
    HotSwapTouchHelper simpleItemTouchCallback;
    RecyclerView.Adapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotswap_add_players);

        hotSwapAddPlayersViewModel = ViewModelProviders.of(this).get(HotSwapAddPlayersViewModel.class);
        getLifecycle().addObserver(hotSwapAddPlayersViewModel);
        hotSwapAddPlayersViewModel.onCreate();

        playerListMutableLiveData = hotSwapAddPlayersViewModel.getPlayerListForView();
        hotSwapAddPlayersViewModel.getPlayerListForView().observe(this, new Observer<List<Pair<Player, Integer>>>() {
            @Override
            public void onChanged(List<Pair<Player, Integer>> playerList) {
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        simpleItemTouchCallback = new HotSwapTouchHelper(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (!(viewHolder.getItemViewType() == 1)) {
                    hotSwapAddPlayersViewModel.removePlayer(viewHolder.getAdapterPosition());
                }
            }
        };
        setupRecyclerView();
    }

    /**
     * Sets up the View with it's helper class HotSwapRecyclerAdapter.
     */
    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hsApPlayersRecyclerView);
        recyclerAdapter = new HotSwapRecyclerAdapter(this, playerListMutableLiveData, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onAddPlayer() {
        hotSwapAddPlayersViewModel.addNewPlayer();
    }

    @Override
    public void onTeamSelected(int position, int newTeamNum) {
        hotSwapAddPlayersViewModel.onTeamChange(position, newTeamNum);
    }

    public void launchBackHotSwapGameModeClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchQuestionClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionView.class);
        startActivity(intent);
    }
}