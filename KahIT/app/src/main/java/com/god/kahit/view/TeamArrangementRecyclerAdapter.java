package com.god.kahit.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

public class TeamArrangementRecyclerAdapter extends RecyclerView.Adapter<TeamArrangementRecyclerAdapter.ViewHolder> {

    private static final String LOG_TAG = TeamArrangementRecyclerAdapter.class.getSimpleName();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private IOnPlayerClickListener iOnplayerclickListener;
    MutableLiveData<List<Player>> playerList; //TODO LIST IS ENOUGH LIVEDATA BETWEEN VIEW AND VM NOT ADAPTER
    List<Integer> teamColors;
    List<String> teamNumbers;

    private Context context;

    public TeamArrangementRecyclerAdapter(Context c, MutableLiveData<List<Player>> playerList, IOnPlayerClickListener iOnplayerclickListener) {
        this.playerList = playerList;
        this.context = c;
        this.iOnplayerclickListener = iOnplayerclickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemSelectedListener{



        IOnPlayerClickListener iOnplayerclickListener;
        public ConstraintLayout row;
        public TextView textView;
        public ImageView img;
        public Button add;
        public Button remove;
        public Spinner spin;
        //View.OnClickListener onClickListener; //TODO


        public ViewHolder(@NonNull View itemView, IOnPlayerClickListener iOnplayerclickListener) {
            super(itemView);
            this.iOnplayerclickListener = iOnplayerclickListener;

            row = (ConstraintLayout) itemView.findViewById(R.id.a_row);
            textView = (TextView) itemView.findViewById(R.id.player_name);
            img = (ImageView) itemView.findViewById(R.id.player_image);
            //add = itemView.findViewById(R.id.add_button);
            remove = itemView.findViewById(R.id.remove_Player_Button1);
            spin = (Spinner) itemView.findViewById(R.id.spinner2);


            add.setOnClickListener(this); //TODO
            remove.setOnClickListener(this);
            //spin.setOnClickListener();
            itemView.setOnClickListener(this);

            initTeamColors();
            initTeamNumbers();

            //spin.setSelection(getIndex(spin, 3));

            spin.setAdapter(new CustomSpinnerAdapter(
                    context,
                    R.layout.spinner_item,
                    R.id.text1,
                    teamNumbers,
                    teamColors));

            spin.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            row.setBackgroundColor(teamColors.get(position));
            parent.setBackgroundColor(teamColors.get(position));
            iOnplayerclickListener.onTeamSelected(getAdapterPosition(), position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public void onClick(View v) {
            iOnplayerclickListener.onPlayerClick(getAdapterPosition());
        }
    }
    private class FooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button btnSubmitProblem;

        public FooterViewHolder(View view) {
            super(view);
            btnSubmitProblem = (Button) view.findViewById(R.id.add_button1);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private void initTeamNumbers() {
        teamNumbers = new ArrayList<>();
        for(int i = 1; i <9;i++) {
            teamNumbers.add(" " + i + " ");
        }
    }

    private void initTeamColors() {
        teamColors =new ArrayList<>();
        int retrieve []=context.getResources().getIntArray(R.array.androidcolors);
        for(int re:retrieve)
        {
            teamColors.add(re);
        }
    }

    @Override
    public TeamArrangementRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view ;
        ViewHolder holder;

        if (viewType == TYPE_ITEM) {
            view = inflater.inflate(
                    R.layout.player_row,
                    parent,
                    false);

        } else {
            view = inflater.inflate(
                    R.layout.game_lobby_adapter_footer,
                    parent,
                    false);
        }


        ViewHolder viewHolder = new ViewHolder(view, iOnplayerclickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        TextView textView = viewHolder.textView;
        ImageView imageView = viewHolder.img;
        Resources res = context.getResources();

        textView.setText(playerList.getValue().get(i).getName());

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
}