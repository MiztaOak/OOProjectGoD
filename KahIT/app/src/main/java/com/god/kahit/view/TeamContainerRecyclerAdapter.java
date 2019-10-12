package com.god.kahit.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.networkManager.Connection;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class TeamContainerRecyclerAdapter extends RecyclerView.Adapter<TeamContainerRecyclerAdapter.ItemViewHolder> {
    private static final String LOG_TAG = TeamContainerRecyclerAdapter.class.getSimpleName(); //todo use same tag name in all classes
    private static final int READY_COLOR_GREEN = 0xAB48D613;
    private static final int READY_COLOR_RED = 0xABd61313;
    private Context context;
    private Team team;
    private MutableLiveData<List<Pair<Player, Connection>>> playerConPairList;
    private MutableLiveData<String> myPlayerId;
    private List<Integer> teamColors;
    private IOnClickPlayerListener iOnClickPlayerListener;
    private boolean isHost;

    public TeamContainerRecyclerAdapter(Context context, Team team, MutableLiveData<List<Pair<Player, Connection>>> playerConPairList, MutableLiveData<String> myPlayerId, boolean isHost, IOnClickPlayerListener iOnClickPlayerListener) {
        this.context = context;
        this.team = team;
        this.playerConPairList = playerConPairList;
        this.myPlayerId = myPlayerId;
        this.iOnClickPlayerListener = iOnClickPlayerListener;
        this.isHost = isHost;

        initTeamColors();
    }

    private void initTeamColors() {
        teamColors = new ArrayList<>();
        int retrieve[] = context.getResources().getIntArray(R.array.androidcolors);
        for (int re : retrieve) {
            teamColors.add(re);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(
                R.layout.player_row_netmulti,
                parent,
                false);

        return new ItemViewHolder(view, iOnClickPlayerListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Player holderPlayer = team.getTeamMembers().get(position);

        //Determine of local player
        boolean isMe = false;
        if (myPlayerId.getValue() != null && myPlayerId.getValue().equals(holderPlayer.getId())) {
            isMe = true;
        }

        //Set a less saturated tint of team color to player row
        int teamColorRGB = getLessSaturatedColor(teamColors.get(Integer.valueOf(team.getId())));
        holder.headerConstraintLayout.setBackgroundTintList(ColorStateList.valueOf(teamColorRGB));

        //Set player name, if local player also add additional string
        holder.playerNameTextView.setText((isMe ? "(ME):" : "") + holderPlayer.getName());

        //Set 'kick'-button to visible if host and not local player
        holder.playerKickButton.setVisibility((isHost && !isMe) ? View.VISIBLE : View.GONE);

        //Set player ready 'icon' based on ready-status
        if (isMe && isHost) {
            holder.playerReadyTextView.setVisibility(View.INVISIBLE);
        }

        if (holderPlayer.isPlayerReady()) {
            holder.playerReadyTextView.setBackgroundTintList(ColorStateList.valueOf(READY_COLOR_GREEN));
            holder.playerReadyTextView.setText("R");
        } else {
            holder.playerReadyTextView.setBackgroundTintList(ColorStateList.valueOf(READY_COLOR_RED));
            holder.playerReadyTextView.setText("N");
        }

        //Set player image
        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.player1); //TODO more pictures.
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
        roundedBitmapDrawable.setCircular(true);
        holder.playerImageView.setImageDrawable(roundedBitmapDrawable);
    }

    @Override
    public int getItemCount() {
        if (team == null) {
            return 0;
        }
        return team.getTeamMembers().size();
    }

    private int getLessSaturatedColor(int teamColorRGB) {
        float[] hsvArr = new float[3];
        Color.colorToHSV(teamColorRGB, hsvArr);
        hsvArr[1] -= 0.7f;
        teamColorRGB = Color.HSVToColor(200, hsvArr);
        return teamColorRGB;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        IOnClickPlayerListener iOnClickPlayerListener;

        private ConstraintLayout headerConstraintLayout;
        private ImageView playerImageView;
        private TextView playerNameTextView;
        private Button playerKickButton;
        private TextView playerReadyTextView;

        public ItemViewHolder(@NonNull View itemView, IOnClickPlayerListener iOnClickPlayerListener) {
            super(itemView);
            this.iOnClickPlayerListener = iOnClickPlayerListener;

            headerConstraintLayout = itemView.findViewById(R.id.playerRow_net_header_constraintLayout);
            playerImageView = itemView.findViewById(R.id.playerRow_image_imageView);
            playerNameTextView = itemView.findViewById(R.id.playerRow_net_name_textView);
            playerReadyTextView = itemView.findViewById(R.id.playerRow_net_ready_textView);
            playerKickButton = itemView.findViewById(R.id.playerRow_net_kick_button);

            playerKickButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iOnClickPlayerListener.onClick(team.getTeamMembers().get(getAdapterPosition()));
        }
    }
}
