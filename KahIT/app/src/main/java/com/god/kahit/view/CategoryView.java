package com.god.kahit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.god.kahit.R;
import com.god.kahit.viewModel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryView extends AppCompatActivity {
    private static final String LOG_TAG = AfterQuestionScorePageView.class.getSimpleName();
    List<ImageButton> buttons;
    CategoryViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        model = ViewModelProviders.of(this).get(CategoryViewModel.class);

        buttons = new ArrayList<>();
        buttons.add((ImageButton) findViewById(R.id.cButton1));
        buttons.add((ImageButton) findViewById(R.id.cButton2));
        buttons.add((ImageButton) findViewById(R.id.cButton3));
        buttons.add((ImageButton) findViewById(R.id.cButton4));

        addPicturesToButton();
    }

    private void addPicturesToButton() {
        for (int i = 0; i < buttons.size(); i++) {
            int resId = getResources().getIdentifier(model.getCategories().get(i).toString() + "icon", "drawable", getApplicationInfo().packageName);
            buttons.get(i).setImageResource(resId);
        }
    }

    public void onCategoryClick(View view) {
        int i = 0;
        if (view instanceof ImageButton) {
            i = buttons.indexOf(view);
        }

        model.setCategory(i);
        launchQuestionClass();
    }

    public void launchQuestionClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}