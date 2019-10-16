package com.god.kahit.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Helper class for the HotSwapAddPlayerView it works as a recyclerAdapter for the RecyclerView.
 */
public class HotSwapRecyclerAdapter extends RecyclerView.Adapter<HotSwapRecyclerAdapter.itemViewHolder> {

    private static final String LOG_TAG = HotSwapRecyclerAdapter.class.getSimpleName();

    private IOnPlayerClickListener iOnplayerclickListener;

    private MutableLiveData<List<Pair<Player, Integer>>> playerList;

    private List<Integer> teamColors;
    private List<String> teamNumbers;

    private Context context;

    public HotSwapRecyclerAdapter(Context c, MutableLiveData<List<Pair<Player, Integer>>> playerList, IOnPlayerClickListener iOnplayerclickListener) {
        this.context = c;
        this.playerList = playerList;
        this.iOnplayerclickListener = iOnplayerclickListener;
    }

    public class itemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemSelectedListener{

        IOnPlayerClickListener iOnplayerclickListener;
        ConstraintLayout row;
        TextView textView;
        ImageView img;
        Spinner spin;
        TextView spinnerText;



        public itemViewHolder(@NonNull View itemView, IOnPlayerClickListener iOnplayerclickListener) {
            super(itemView);
            this.iOnplayerclickListener = iOnplayerclickListener;

            row = itemView.findViewById(R.id.a_row);
            textView = itemView.findViewById(R.id.player_name);
            img = itemView.findViewById(R.id.player_image);
            spin = itemView.findViewById(R.id.spinner2);
            spinnerText = itemView.findViewById(R.id.text1);

            initTeamColors();
            initTeamNumbers();

            spin.setAdapter(new CustomSpinnerAdapter(
                    context,
                    R.layout.spinner_item_hotswap,
                    R.id.text1,
                    teamNumbers,
                    teamColors));

            spin.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int i = getAdapterPosition();
            if(position != (Objects.requireNonNull(playerList.getValue()).get(getAdapterPosition()).second - 1)) {
                iOnplayerclickListener.onTeamSelected(getAdapterPosition(), position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

        @Override
        public void onClick(View v) {
            iOnplayerclickListener.onPlayerClick(getAdapterPosition());
        }

        ItemTouchHelper.SimpleCallback touchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                iOnplayerclickListener.onPlayerClick(i);
            }
        };
    }

    private void initTeamNumbers() {
        teamNumbers = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            teamNumbers.add(" " + i + " ");
        }
    }

    private void initTeamColors() {
        teamColors = new ArrayList<>();
        int retrieve[] = context.getResources().getIntArray(R.array.androidcolors);
        for (int re : retrieve) {
            teamColors.add(re);
        }
    }

    private int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

    @Override
    public itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view ;

            view = inflater.inflate(
                    R.layout.player_row,
                    parent,
                    false);

        return new itemViewHolder(view, iOnplayerclickListener);
    }

    @Override
    public void onBindViewHolder(itemViewHolder itemViewHolder, int i) {
        TextView textView = itemViewHolder.textView;
        ImageView imageView = itemViewHolder.img;
        Resources res = context.getResources();

        itemViewHolder.spin.setSelection(Objects.requireNonNull(playerList.getValue()).get(i).second - 1);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(manipulateColor(teamColors.get(itemViewHolder.spin.getSelectedItemPosition()), 0.8f));
        gd.setCornerRadius(30);
        itemViewHolder.row.setBackground(gd);
        itemViewHolder.spin.setBackground(gd);

        textView.setText(playerList.getValue().get(i).first.getName());

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.player1); //TODO more pictures.
        imageView.setImageDrawable(drawable);
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.player1);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    @Override
    public int getItemCount() {
        if (null == playerList.getValue() || playerList.getValue().size() == 0) {
            return 0;
        }
        return playerList.getValue().size();
    }
}