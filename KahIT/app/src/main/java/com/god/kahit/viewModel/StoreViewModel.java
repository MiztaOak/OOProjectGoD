package com.god.kahit.viewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Item;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class StoreViewModel extends ViewModel {
    public boolean isItemBuyable(int i) {
        return Repository.getInstance().isItemBuyable(i);
    }

    public void buy(int i) {
        Repository.getInstance().buy(i);
    }

    public List<Item> getStoreItems() {
        return Repository.getInstance().getStoreItems();
    }

    public String getItemName(int i) {
        return Repository.getInstance().getItemName(i);
    }

    public int getItemPrice(int i) {
        return Repository.getInstance().getItemPrice(i);
    }

    public int getPlayerPoints() {
        return Repository.getInstance().getPlayerScore();
    }

    public boolean isItemBought(int i) {

        return Repository.getInstance().isItemBought(i);
    }
}
