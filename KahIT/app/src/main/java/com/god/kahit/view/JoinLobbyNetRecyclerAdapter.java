package com.god.kahit.view;

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

/**
 * responsibility: Helper class for the JoinLobbyNetView. Responsible for the recyclerView that displays the different lobbies.
 * used-by: JoinLobbyNetView
 *
 * @author Oussama Anadani, Jakob Ewerstrand
 */
public class JoinLobbyNetRecyclerAdapter extends RecyclerView.Adapter<JoinLobbyNetRecyclerAdapter.ItemViewHolder> {
    private static final String TAG = JoinLobbyNetRecyclerAdapter.class.getSimpleName();
    private final MutableLiveData<List<Connection>> lobbyList;
    private final IOnClickLobbyListener iOnClickListener;

    public JoinLobbyNetRecyclerAdapter(MutableLiveData<List<Connection>> lobbyList, IOnClickLobbyListener iOnClickListener) {
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
        private final IOnClickLobbyListener iOnClickListener;
        private final TextView lobbyIdTextView;
        private final TextView lobbyNameTextView;
        private final TextView lobbyStatusTextView;

        ItemViewHolder(@NonNull View itemView, IOnClickLobbyListener iOnClickListener) {
            super(itemView);
            this.iOnClickListener = iOnClickListener;

            ConstraintLayout constraintLayout = itemView.findViewById(R.id.lobby_row_constraintLayout);
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
