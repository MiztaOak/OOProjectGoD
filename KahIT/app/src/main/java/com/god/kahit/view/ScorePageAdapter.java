package com.god.kahit.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.model.Player;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScorePageAdapter extends RecyclerView.Adapter<ScorePageAdapter.ViewHolder> {
    private Map<String, String> imageNameMap = ItemDataLoaderRealtime.getItemImageNameMap();
    private List<Player> playerList;
    private String myPlayerId;
    private boolean isHotSwap;

    ScorePageAdapter(List<Player> playerList, String myPlayerId, boolean isHotSwap) {
        this.playerList = playerList;
        this.myPlayerId = myPlayerId;
        this.isHotSwap = isHotSwap;
    }

    @NonNull
    @Override
    public ScorePageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.score_screen_row, parent, false);

        ScorePageAdapter.ViewHolder viewHolder = new ScorePageAdapter.ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TextView nameView = viewHolder.name;
        TextView scoreDelta = viewHolder.score;
        ImageView playerImg = viewHolder.img;

        Player player = playerList.get(i);

        //Set player name
        String nameString;
        if (!isHotSwap && player.getId().equals(myPlayerId)) {
            nameString = String.format("ME:%s", player.getName());
        } else {
            nameString = String.format("%s", player.getName());
        }
        nameView.setText(nameString);

        //Set player score
        scoreDelta.setText(String.format("%d", playerList.get(i).getScore()));

        //Set player image
        if (player.getVanityItem() != null) {
            int resId = 0;
            try {
                resId = R.drawable.class.getField(imageNameMap.get(player.getVanityItem().getName())).getInt(null);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            playerImg.setImageResource(resId);
        }
    }

    @Override
    public int getItemCount() {
        if (playerList != null) {
            return playerList.size();
        } else {
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView score;
        private ImageView img;

        ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.ssrPlayerName);
            score = view.findViewById(R.id.ssrScoreDelta);
            img = view.findViewById(R.id.ssrPlayerIcon);
        }
    }

}
