package com.god.kahit.viewModel;

import com.god.kahit.model.Item;
import com.god.kahit.model.Store;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoreViewModel extends ViewModel {
    public int i = 0;
    private Store storeModel = new Store();
    private MutableLiveData<Integer> points = new MutableLiveData<>();

    public void buy(int i) {
        if (!(points.getValue() == (null)) && points.getValue() >= storeModel.getStoreItems().get(i).getPrice()) {
            points.setValue(points.getValue() - (storeModel.getStoreItems().get(i).getPrice()));
        }
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

    public MutableLiveData<Integer> getPoints() {
        return points;
    }

    public void setPoints(int p) {
        if ((points.getValue() == (null))) {
            this.points.setValue(p);
        } else {
            this.points.setValue(points.getValue() + p);
        }
    }
}
