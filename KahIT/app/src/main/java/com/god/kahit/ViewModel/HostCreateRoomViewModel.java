package com.god.kahit.ViewModel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HostCreateRoomViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = HostCreateRoomViewModel.class.getSimpleName();

    MutableLiveData<List<String>> listForView;
    public HostCreateRoomViewModel() {
    }

    public MutableLiveData<List<String>> getListForView() {
        if (listForView == null) {
            listForView = new MutableLiveData<>();
        }
        return listForView;
    }
}
