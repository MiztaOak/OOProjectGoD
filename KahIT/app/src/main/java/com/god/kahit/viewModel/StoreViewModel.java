package com.god.kahit.viewModel;

import com.god.kahit.model.Item;
import com.god.kahit.model.Store;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class StoreViewModel extends ViewModel {
    public int i = 0;
    private Store storeModel = new Store();
    public boolean isItemBuyable(Item item){
        return (!(item.getPrice() > storeModel.getPlayer().getScore()));
    }
    public void buy(int i) {
        storeModel.getPlayer().setScore((storeModel.getPlayer().getScore() - (storeModel.getStoreItems().get(i).getPrice())));
        storeModel.buy(storeModel.getStoreItems().get(i), storeModel.getPlayer());
    }

    public List<Item> getStoreItems() {
        return storeModel.getStoreItems();
    }

    public Store getStoreModel() {
        return storeModel;
    }

    public void setStoreModel(Store storeModel) {
        this.storeModel = storeModel;
    }
}
