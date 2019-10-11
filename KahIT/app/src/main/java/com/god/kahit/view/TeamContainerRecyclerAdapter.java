package com.god.kahit.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Player;
import com.god.kahit.model.Team;
import com.god.kahit.networkManager.Connection;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class TeamContainerRecyclerAdapter extends RecyclerView.Adapter<TeamContainerRecyclerAdapter.ItemViewHolder> {
    private static final String LOG_TAG = TeamContainerRecyclerAdapter.class.getSimpleName(); //todo use same tag name in all classes
    private Context context;
    private Team team;
    private MutableLiveData<List<Pair<Player, Connection>>> playerConPairList;
    private List<Integer> teamColors;
    private IOnClickListener iOnClickListener;
    private boolean showKickButton;

    public TeamContainerRecyclerAdapter(Context context, Team team, MutableLiveData<List<Pair<Player, Connection>>> playerConPairList, boolean showKickButton, IOnClickListener iOnClickListener) {
        this.context = context;
        this.team = team;
        this.playerConPairList = playerConPairList;
        this.iOnClickListener = iOnClickListener;
        this.showKickButton = showKickButton;

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

        return new ItemViewHolder(view, iOnClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Player holderPlayer = team.getTeamMembers().get(position);

        holder.playerNameTextView.setText(holderPlayer.getName());
        holder.playerKickButton.setVisibility(showKickButton ? View.VISIBLE : View.GONE);

        if (holderPlayer.isPlayerReady()) {
            holder.playerReadyTextView.setBackgroundTintList(ColorStateList.valueOf(0xAB48D613));
            holder.playerReadyTextView.setText("R");
        } else {
            holder.playerReadyTextView.setBackgroundTintList(ColorStateList.valueOf(0xABd61313));
            holder.playerReadyTextView.setText("N");
        }

        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.player1); //TODO more pictures.
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
        roundedBitmapDrawable.setCircular(true);
        holder.playerImageView.setImageDrawable(roundedBitmapDrawable);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        IOnClickListener iOnClickListener;

        private ImageView playerImageView;
        private TextView playerNameTextView;
        private Button playerKickButton;
        private TextView playerReadyTextView;

        public ItemViewHolder(@NonNull View itemView, IOnClickListener iOnClickListener) {
            super(itemView);
            this.iOnClickListener = iOnClickListener;

            playerImageView = itemView.findViewById(R.id.playerRow_image_imageView);
            playerNameTextView = itemView.findViewById(R.id.playerRow_net_name_textView);
            playerReadyTextView = itemView.findViewById(R.id.playerRow_net_ready_textView);
            playerKickButton = itemView.findViewById(R.id.playerRow_net_kick_button);

            playerKickButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iOnClickListener.onClick(team.getTeamMembers().get(getAdapterPosition()));
        }
    }
}
