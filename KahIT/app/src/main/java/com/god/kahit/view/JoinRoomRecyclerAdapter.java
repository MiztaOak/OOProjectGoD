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

public class JoinRoomRecyclerAdapter extends RecyclerView.Adapter<JoinRoomRecyclerAdapter.ItemViewHolder> {
    private static final String TAG = JoinRoomRecyclerAdapter.class.getSimpleName();
    private MutableLiveData<List<Connection>> roomList;
    private IOnClickRoomListener iOnClickListener;
    private Context context;

    public JoinRoomRecyclerAdapter(Context context, MutableLiveData<List<Connection>> roomList, IOnClickRoomListener iOnClickListener) {
        this.context = context;
        this.roomList = roomList;
        this.iOnClickListener = iOnClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(
                R.layout.room_row,
                parent,
                false);

        return new ItemViewHolder(view, iOnClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Connection holderConnection = Objects.requireNonNull(roomList.getValue()).get(position);

        holder.roomNameTextView.setText(String.format("%s - %s", holderConnection.getId(), holderConnection.getName()));
    }

    @Override
    public int getItemCount() {
        if (null == roomList.getValue() || roomList.getValue().size() == 0) {
            return 0;
        }
        return roomList.getValue().size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private IOnClickRoomListener iOnClickListener;
        private ConstraintLayout constraintLayout;
        private TextView roomNameTextView;

        public ItemViewHolder(@NonNull View itemView, IOnClickRoomListener iOnClickListener) {
            super(itemView);
            this.iOnClickListener = iOnClickListener;

            roomNameTextView = itemView.findViewById(R.id.room_row_name);
            constraintLayout = itemView.findViewById(R.id.room_row_constraintLayout);

            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (roomList.getValue() != null) {
                iOnClickListener.onClick(roomList.getValue().get(getAdapterPosition()));
            } else {
                Log.i(TAG, "onClick: Attempt to get null roomList, skipping click event");
            }
        }
    }
}
