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
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Team;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class LobbyNetRecyclerAdapter extends RecyclerView.Adapter<LobbyNetRecyclerAdapter.ItemViewHolder> {
    private static final String LOG_TAG = LobbyNetRecyclerAdapter.class.getSimpleName();
    private MutableLiveData<List<Team>> teamList;
    private List<Integer> teamColors;
    private IOnClickListener iOnClickListener;
    private Context context;

    public LobbyNetRecyclerAdapter(Context c, MutableLiveData<List<Team>> teamList, IOnClickListener iOnClickListener) {
        this.teamList = teamList;
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
                R.layout.team_container,
                parent,
                false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view, iOnClickListener);
        return itemViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull LobbyNetRecyclerAdapter.ItemViewHolder holder, int i) {
        Team holderTeam = Objects.requireNonNull(teamList.getValue()).get(i);

        holder.teamHeaderConstraintLayout.setBackgroundTintList(ColorStateList.valueOf(teamColors.get(Integer.valueOf(holderTeam.getId()))));
        holder.teamNameTextView.setText(holderTeam.getTeamName());
        holder.teamNameTextInputLayout.setVisibility(View.GONE);
        holder.teamNameTextView.setVisibility(View.VISIBLE);
//        holder.teamPlayersRecyclerView


        /*Resources res = context.getResources();

        textView.setText(teamList.getValue().get(i).getTeamName());

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.player1); //TODO more pictures.
        imageView.setImageDrawable(drawable);
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.player1);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);*/
    }

    @Override
    public int getItemViewType(int position) {
        return (position == teamList.getValue().size()) ? 1 : 0; //todo footer
    }

    @Override
    public int getItemCount() {
        if (null == teamList.getValue() || teamList.getValue().size() == 0) {
            return 0;
        }
        return teamList.getValue().size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        IOnClickListener iOnClickListener;
        private ConstraintLayout teamHeaderConstraintLayout;
        private TextView teamNameTextView;
        private TextInputLayout teamNameTextInputLayout;
        private TextInputEditText teamNameTextInputEditText;
        private RecyclerView teamPlayersRecyclerView;

        public ItemViewHolder(@NonNull View itemView, IOnClickListener iOnClickListener) {
            super(itemView);
//            this.iOnClickListener = iOnClickListener;

            teamHeaderConstraintLayout = itemView.findViewById(R.id.constraintLayout_teamHeader);
            teamNameTextView = itemView.findViewById(R.id.textView_teamName);
            teamNameTextInputLayout = itemView.findViewById(R.id.textInput_teamName);
            teamNameTextInputEditText = itemView.findViewById(R.id.textInputEditText_teamName);
            teamPlayersRecyclerView = itemView.findViewById(R.id.teamContainer_PlayerRecyclerView);
        }
    }
}