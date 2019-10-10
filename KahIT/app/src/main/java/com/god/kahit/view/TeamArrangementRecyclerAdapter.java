package com.god.kahit.view;

import android.content.Context;
import android.content.res.ColorStateList;
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
import com.god.kahit.model.Player;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class TeamArrangementRecyclerAdapter extends RecyclerView.Adapter<TeamArrangementRecyclerAdapter.ItemViewHolder> {
    private static final String LOG_TAG = TeamArrangementRecyclerAdapter.class.getSimpleName();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_DIVIDER = 2;
    private MutableLiveData<List<Pair<Player, Integer>>> playerList;
    private List<Integer> teamColors;
    private IOnClickListener iOnClickListener;
    private Context context;

    public TeamArrangementRecyclerAdapter(Context c, MutableLiveData<List<Pair<Player, Integer>>> playerList, IOnClickListener iOnClickListener) {
        this.playerList = playerList;
        this.context = c;
        this.iOnClickListener = iOnClickListener;

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
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(
                R.layout.player_row,
                parent,
                false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view, iOnClickListener);
        return itemViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull TeamArrangementRecyclerAdapter.ItemViewHolder holder, int i) {
        TextView textView = holder.textView;
        ImageView imageView = holder.img;

        Resources res = context.getResources();





//        Integer value = playerList.getValue().get(i).second;
//        if (value != null) {
//            ItemViewHolder.spin.setSelection(value);
//        }
//
////        ItemViewHolder.row.setBackgroundColor(teamColors.get(ItemViewHolder.spin.getSelectedItemPosition()));
//        ItemViewHolder.spin.setBackgroundColor(teamColors.get(ItemViewHolder.));

        textView.setText(playerList.getValue().get(i).first.getName());

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.player1); //TODO more pictures.
        imageView.setImageDrawable(drawable);
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.player1);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == playerList.getValue().size()) ? 1 : 0; //todo footer
    }

    @Override
    public int getItemCount() {
        if (null == playerList.getValue() || playerList.getValue().size() == 0) {
            return 0;
        }
        return playerList.getValue().size(); // + 1 //TODO COMMENT Footer
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        IOnClickListener iOnClickListener;
        private TextView textView;
        private ImageView img;
        private Button remove;
        private ConstraintLayout player_team_layout;
        private TextView player_team_textView;


        public ItemViewHolder(@NonNull View itemView, IOnClickListener iOnClickListener) {
            super(itemView);
            this.iOnClickListener = iOnClickListener;

            textView = itemView.findViewById(R.id.player_name);
            img = itemView.findViewById(R.id.player_image);
            remove = itemView.findViewById(R.id.remove_Player_Button1);
            player_team_layout = itemView.findViewById(R.id.player_team_layout);
            player_team_textView = itemView.findViewById(R.id.player_team_textView);

            remove.setOnClickListener(this);

            player_team_layout.setBackgroundTintList(ColorStateList.valueOf(teamColors.get(1)));
            player_team_textView.setText("1");

            /*spin.setAdapter(new CustomSpinnerAdapter(
                    context,
                    R.layout.spinner_item,
                    R.id.text1,
                    teamNumbers,
                    teamColors));

            spin.setOnItemSelectedListener(this);*/
        }

        @Override
        public void onClick(View v) {
            iOnClickListener.onClick(getAdapterPosition());
        }
    }
}