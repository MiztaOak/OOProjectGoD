package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.god.kahit.R;
import com.god.kahit.ViewModel.HostCreateRoomViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

public class HostCreateRoomView extends AppCompatActivity {

    private static final String LOG_TAG = HostCreateRoomView.class.getSimpleName();

    MutableLiveData<List<String>> list;
    HostCreateRoomViewModel hostCreateRoomViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_create_room_activity);

        hostCreateRoomViewModel = ViewModelProviders.of(this).get(HostCreateRoomViewModel.class);
        list = hostCreateRoomViewModel.getListForView();
        hostCreateRoomViewModel.getListForView().observe(this, new Observer<List<String>>() {

            @Override
            public void onChanged(@Nullable List<String> integerStringMap) {
            }
        });
    }

    public void launchBackChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameClass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchQuestionClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionClass.class);
        startActivity(intent);
    }

    public void launchTeamArrangementActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, TeamArrangementView.class);
        startActivity(intent);
    }
}
