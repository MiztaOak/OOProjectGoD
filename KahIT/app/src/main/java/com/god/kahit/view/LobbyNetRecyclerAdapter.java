package com.god.kahit.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.god.kahit.R;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.networkManager.Connection;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * responsibility: Helper class responsible for the recyclerView inside the TeamContainerRecyclerAdapter.
 * used-by: LobbyNetView.
 *
 * @author Mats Cedervall.
 */
public class LobbyNetRecyclerAdapter extends RecyclerView.Adapter<LobbyNetRecyclerAdapter.ItemViewHolder> {
    private static final String LOG_TAG = LobbyNetRecyclerAdapter.class.getSimpleName();
    private MutableLiveData<List<Team>> teamList;
    private MutableLiveData<List<Pair<Player, Connection>>> playerConPairList;
    private MutableLiveData<String> myPlayerId;
    private List<Integer> teamColors;
    private boolean isHost;
    private IOnClickPlayerListener iOnClickPlayerListener;
    private Context context;

    public LobbyNetRecyclerAdapter(Context c, MutableLiveData<List<Team>> teamList, MutableLiveData<List<Pair<Player, Connection>>> playerConPairList, MutableLiveData<String> myPlayerId, boolean isHost, IOnClickPlayerListener iOnClickPlayerListener) {
        this.context = c;
        this.teamList = teamList;
        this.playerConPairList = playerConPairList;
        this.myPlayerId = myPlayerId;
        this.isHost = isHost;
        this.iOnClickPlayerListener = iOnClickPlayerListener;

        initTeamColors();
    }

    private void initTeamColors() {
        teamColors = new ArrayList<>();
        int[] retrieve = context.getResources().getIntArray(R.array.androidcolors);
        for (int re : retrieve) {
            teamColors.add(re);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(
                R.layout.team_container,
                parent,
                false);

        return new ItemViewHolder(view, iOnClickPlayerListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull LobbyNetRecyclerAdapter.ItemViewHolder holder, int i) {
        Team holderTeam = Objects.requireNonNull(teamList.getValue()).get(i);

        holder.teamHeaderConstraintLayout.setBackgroundTintList(ColorStateList.valueOf(teamColors.get(Integer.valueOf(holderTeam.getId()) - 1)));
        holder.teamNameTextView.setText(holderTeam.getTeamName());
        holder.teamNameTextInputLayout.setVisibility(View.GONE);
        holder.teamNameTextView.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        TeamContainerRecyclerAdapter recyclerAdapter = new TeamContainerRecyclerAdapter(context, holderTeam, playerConPairList, myPlayerId, isHost, iOnClickPlayerListener);
        holder.teamPlayersRecyclerView.setAdapter(recyclerAdapter);
        holder.teamPlayersRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == teamList.getValue().size()) ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        if (null == teamList.getValue() || teamList.getValue().size() == 0) {
            return 0;
        }
        return teamList.getValue().size();
    }

    /**
     *
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private IOnClickPlayerListener iOnClickPlayerListener;
        private ConstraintLayout teamHeaderConstraintLayout;
        private TextView teamNameTextView;
        private TextInputLayout teamNameTextInputLayout;
        private TextInputEditText teamNameTextInputEditText;
        private RecyclerView teamPlayersRecyclerView;

        public ItemViewHolder(@NonNull View itemView, IOnClickPlayerListener iOnClickPlayerListener) {
            super(itemView);
            this.iOnClickPlayerListener = iOnClickPlayerListener;

            teamHeaderConstraintLayout = itemView.findViewById(R.id.constraintLayout_teamHeader);
            teamNameTextView = itemView.findViewById(R.id.textView_teamName);
            teamNameTextInputLayout = itemView.findViewById(R.id.textInput_teamName);
            teamNameTextInputEditText = itemView.findViewById(R.id.textInputEditText_teamName);
            teamPlayersRecyclerView = itemView.findViewById(R.id.teamContainer_PlayerRecyclerView);
        }
    }
}