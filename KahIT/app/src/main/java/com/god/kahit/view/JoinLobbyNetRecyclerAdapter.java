package com.god.kahit.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.networkManager.Connection;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class JoinLobbyNetRecyclerAdapter extends RecyclerView.Adapter<JoinLobbyNetRecyclerAdapter.ItemViewHolder> {
    private static final String TAG = JoinLobbyNetRecyclerAdapter.class.getSimpleName();
    private MutableLiveData<List<Connection>> lobbyList;
    private IOnClickLobbyListener iOnClickListener;
    private Context context;

    public JoinLobbyNetRecyclerAdapter(Context context, MutableLiveData<List<Connection>> lobbyList, IOnClickLobbyListener iOnClickListener) {
        this.context = context;
        this.lobbyList = lobbyList;
        this.iOnClickListener = iOnClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(
                R.layout.lobby_row,
                parent,
                false);

        return new ItemViewHolder(view, iOnClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Connection holderConnection = Objects.requireNonNull(lobbyList.getValue()).get(position);

        holder.lobbyIdTextView.setText(holderConnection.getId());
        holder.lobbyNameTextView.setText(holderConnection.getName());
        holder.lobbyStatusTextView.setText(holderConnection.getState().toString());
    }

    @Override
    public int getItemCount() {
        if (null == lobbyList.getValue() || lobbyList.getValue().size() == 0) {
            return 0;
        }
        return lobbyList.getValue().size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private IOnClickLobbyListener iOnClickListener;
        private ConstraintLayout constraintLayout;
        private TextView lobbyIdTextView;
        private TextView lobbyNameTextView;
        private TextView lobbyStatusTextView;

        public ItemViewHolder(@NonNull View itemView, IOnClickLobbyListener iOnClickListener) {
            super(itemView);
            this.iOnClickListener = iOnClickListener;

            constraintLayout = itemView.findViewById(R.id.lobby_row_constraintLayout);
            lobbyIdTextView = itemView.findViewById(R.id.lobby_row_id);
            lobbyNameTextView = itemView.findViewById(R.id.lobby_row_name);
            lobbyStatusTextView = itemView.findViewById(R.id.lobby_row_status);

            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (lobbyList.getValue() != null) {
                iOnClickListener.onClick(lobbyList.getValue().get(getAdapterPosition()));
            } else {
                Log.i(TAG, "onClick: Attempt to get null lobbyList, skipping click event");
            }
        }
    }
}
