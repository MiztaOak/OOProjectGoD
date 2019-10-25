package com.god.kahit.view;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * responsibility: A helper class for the different RecyclerAdapters. Functions as a custom "team-spinner/selector" in the lobby.
 * <p>
 * used-by: HotSwapRecyclerAdapter, LobbyNetView.
 *
 * @author Jakob Ewerstrand
 */
class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private final List<Integer> mColors;


    CustomSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects, List<Integer> mColors) {
        super(context, resource, textViewResourceId, objects);
        this.mColors = mColors;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row;

        row = super.getDropDownView(position, convertView, parent);
        row.setBackgroundColor(mColors.get(position));

        return row;
    }
}

