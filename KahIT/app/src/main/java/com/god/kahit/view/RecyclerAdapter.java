package com.god.kahit.view;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;

import java.util.Map;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String LOG_TAG = RecyclerAdapter.class.getSimpleName();
    MutableLiveData<Map<Integer, String>> playerMap;
    private Context context;

    public RecyclerAdapter(Context c, MutableLiveData<Map<Integer, String>> playerMap) {
        this.playerMap = playerMap;
        this.context = c;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.player_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        int pos = i + 1;
        TextView textView = viewHolder.textView;
        ImageView imageView = viewHolder.img;

        if (null != playerMap.getValue()) {
            textView.setText(playerMap.getValue().get(pos));
        }

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.player1); //TODO more pictures.
        imageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        if (null != playerMap.getValue()) {
            return playerMap.getValue().size();
        } else {
            return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ConstraintLayout row;
        public TextView textView;
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            row = itemView.findViewById(R.id.a_row);
            textView = itemView.findViewById(R.id.player_name);
            img = itemView.findViewById(R.id.player_image);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            //Toast.makeText(v.getContext(), playerMap.getValue().get(pos), Toast.LENGTH_LONG).show(); //TODO
        }
    }
}
