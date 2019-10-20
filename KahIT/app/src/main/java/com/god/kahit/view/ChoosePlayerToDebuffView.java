package com.god.kahit.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.god.kahit.R;
import com.god.kahit.viewModel.ChoosePlayerToDebuffViewModel;

import java.util.ArrayList;

public class ChoosePlayerToDebuffView extends Fragment {
    private ChoosePlayerToDebuffViewModel viewModel = new ChoosePlayerToDebuffViewModel();
    private HotSwapRecyclerAdapter adapter;

    public static ChoosePlayerToDebuffView newInstance() {
        return new ChoosePlayerToDebuffView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_player_to_debuff, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChoosePlayerToDebuffViewModel.class);
        ArrayList<String> players = new ArrayList<>();
        players.add("Anas");
        players.add("Jakob");
        players.add("Mats");
        players.add("Oussama");
        players.add("Johan");
        RecyclerView recyclerView = getView().findViewById(R.id.playersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //adapter = new HotSwapRecyclerAdapter(this.getContext(), players);
        //recyclerView.setAdapter(adapter);
        // TODO: Use the ViewModel
    }
}
