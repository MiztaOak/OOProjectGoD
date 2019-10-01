package com.god.kahit.view;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Tuple;

import java.util.List;
import java.util.Map;

public class ScorePageAdapter extends RecyclerView.Adapter<ScorePageAdapter.ViewHolder> {
    private List<Tuple<String,String>> playerScoreDeltaList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView score;
        public ImageView img;

        public ViewHolder(@NonNull View view){
            super(view);
            name = view.findViewById(R.id.ssrPlayerName);
            score = view.findViewById(R.id.ssrScoreDelta);
            img = view.findViewById(R.id.ssrPlayerIcon);
        }
    }

    public ScorePageAdapter(List<Tuple<String,String>> playerScoreDeltaList){
        this.playerScoreDeltaList = playerScoreDeltaList;
    }

    @NonNull
    @Override
    public ScorePageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.score_screen_row, parent, false);

        ScorePageAdapter.ViewHolder viewHolder = new ScorePageAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TextView nameView = viewHolder.name;
        TextView scoreDelta = viewHolder.score;

        nameView.setText(playerScoreDeltaList.get(i).getX());
        scoreDelta.setText(playerScoreDeltaList.get(i).getY());
    }

    @Override
    public int getItemCount() {
        if(playerScoreDeltaList != null) {
            return playerScoreDeltaList.size();
        }
        else{
            return 0;
        }
    }

}
