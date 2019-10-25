package com.god.kahit.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.god.kahit.R;

/**
 * responsibility: This class shows a page that contains an explanation about the app.
 * used-by: MainActivityView.
 *
 * @author Oussama Anadani
 */
public class AboutKahitView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_kahit);
    }
}
