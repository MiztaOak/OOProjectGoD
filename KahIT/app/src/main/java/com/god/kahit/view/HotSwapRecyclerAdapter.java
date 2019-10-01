package com.god.kahit.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Helper class for the HotSwapAddPlayerView it works as a recyclerAdapter for the RecyclerView.
 */
public class HotSwapRecyclerAdapter extends RecyclerView.Adapter<HotSwapRecyclerAdapter.ViewHolder> {
    private static final String LOG_TAG = HotSwapRecyclerAdapter.class.getSimpleName();
    MutableLiveData<List<String>> playerList;
    private Context context;

    public HotSwapRecyclerAdapter(Context c, MutableLiveData<List<String>> playerList) {
        this.playerList = playerList;
        this.context = c;
    }

    @Override
    public HotSwapRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.player_row, parent, false);
        } else {
            view = inflater.inflate(R.layout.player_row, parent, false);
            view.findViewById(R.id.add_button).setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.drawable.dotted);
            view.findViewById(R.id.player_image).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.remove_Player_Button).setVisibility(View.INVISIBLE);
        }


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        TextView textView = viewHolder.textView;
        ImageView imageView = viewHolder.img;
        Resources res = context.getResources();

        if (i < 8) {
            String s;
            if (null != playerList.getValue() && playerList.getValue().size() != viewHolder.getAdapterPosition()) {
                s = playerList.getValue().get(i);

            } else {
                s = "Add new player";
            }
            textView.setText(s);

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.player1); //TODO more pictures.
            imageView.setImageDrawable(drawable);
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.player1);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
        } else {
            viewHolder.row.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == playerList.getValue().size()) ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        if (null == playerList.getValue()) {
            return 0;
        }
        if (playerList.getValue().size() == 0) {
            return 0;
        }
        return playerList.getValue().size() + 1;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ConstraintLayout row;
        public TextView textView;
        public ImageView img;
        public Button add;
        public Button remove;
        View.OnClickListener onClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            row = (ConstraintLayout) itemView.findViewById(R.id.a_row);
            textView = (TextView) itemView.findViewById(R.id.player_name);
            img = (ImageView) itemView.findViewById(R.id.player_image);
            add = itemView.findViewById(R.id.add_button);
            remove = itemView.findViewById(R.id.remove_Player_Button);

            add.setOnClickListener(this);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}