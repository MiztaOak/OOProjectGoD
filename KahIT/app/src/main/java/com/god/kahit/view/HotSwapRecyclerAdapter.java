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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.god.kahit.R;
import com.god.kahit.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * responsibility: Helper class for the HotSwapAddPlayerView it functions as a recyclerAdapter for the RecyclerView.
 * <p>
 * used-by: HotSwapAddPlayersView
 *
 * @author Jakob Ewerstrand
 */
public class HotSwapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = HotSwapRecyclerAdapter.class.getSimpleName();

    private static final int FOOTER_VIEW = 1;

    private IHotSwapViewHolderClickListener iHotSwapViewHolderClickListener;

    private MutableLiveData<List<Pair<Player, Integer>>> playerList;

    private List<Integer> teamColors;
    private List<String> teamNumbers;

    private Context context;

    HotSwapRecyclerAdapter(Context c, MutableLiveData<List<Pair<Player, Integer>>> playerList, IHotSwapViewHolderClickListener iHotSwapViewHolderClickListener) {
        this.context = c;
        this.playerList = playerList;
        this.iHotSwapViewHolderClickListener = iHotSwapViewHolderClickListener;
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

    /**
     * Adjusts the colors brightness based on param.
     *
     * @param color  The color to be adjusted.
     * @param factor - the factor that you want to change the color with e.g. 1.1f brightens, 0.9f darkens.
     * @return - The adjusted value as an int.
     */
    private int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view;

        if (viewType == FOOTER_VIEW) {
            view = inflater.inflate(
                    R.layout.hs_lobby_footer,
                    parent,
                    false);
            return new FooterViewHolder(view, iHotSwapViewHolderClickListener);
        } else {
            view = inflater.inflate(
                    R.layout.player_row,
                    parent,
                    false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        try {
            if (viewHolder instanceof ItemViewHolder) {
                TextView textView = ((ItemViewHolder) viewHolder).textView;
                ImageView imageView = ((ItemViewHolder) viewHolder).img;
                Resources res = context.getResources();

                ((ItemViewHolder) viewHolder).spin.setSelection(Objects.requireNonNull(playerList.getValue()).get(i).second - 1);

                setRowLayout((ItemViewHolder) viewHolder);

                textView.setText(playerList.getValue().get(i).first.getName());

                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.player1);
                imageView.setImageDrawable(drawable);
                Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.player1);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
                roundedBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that sets the rows & spinners corners and it's color.
     *
     * @param itemViewHolder - the row to be adjusted.
     */
    private void setRowLayout(ItemViewHolder itemViewHolder) {
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(30);
        gd.setColor(manipulateColor(teamColors.get(itemViewHolder.spin.getSelectedItemPosition()), 0.8f));
        itemViewHolder.row.setBackground(gd);
        itemViewHolder.spin.setBackground(gd);
    }

    @Override
    public int getItemCount() {
        if (null == playerList.getValue() || playerList.getValue().size() == 0) {
            return 0;
        }
        return playerList.getValue().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == Objects.requireNonNull(playerList.getValue()).size()) ? 1 : 0;
    }


    /**
     * Inner class that functions as the primary row type in the recyclerView.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {

        ConstraintLayout row;
        TextView textView;
        ImageView img;
        Spinner spin;
        TextView spinnerText;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);

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
            if (position != (Objects.requireNonNull(playerList.getValue()).get(getAdapterPosition()).second - 1)) {
                iHotSwapViewHolderClickListener.onTeamSelected(getAdapterPosition(), position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    /**
     * Inner class that functions as a footer for the recyclerView.
     */
    private class FooterViewHolder extends RecyclerView.ViewHolder {

        IHotSwapViewHolderClickListener iHotSwapViewHolderClickListener;

        FooterViewHolder(View view, final IHotSwapViewHolderClickListener iHotSwapViewHolderClickListener) {
            super(view);
            this.iHotSwapViewHolderClickListener = iHotSwapViewHolderClickListener;

            Button addPlayer = view.findViewById(R.id.footerButton);
            addPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iHotSwapViewHolderClickListener.onAddPlayer();
                }
            });
        }
    }
}