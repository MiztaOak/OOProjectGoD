package com.god.kahit.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.god.kahit.Events.AllPlayersReadyEvent;
import com.god.kahit.Events.CategoryVoteResultEvent;
import com.god.kahit.Events.GameLostConnectionEvent;
import com.god.kahit.Events.NewViewEvent;
import com.god.kahit.Events.PlayerVotedCategoryEvent;
import com.god.kahit.R;
import com.god.kahit.viewModel.CategoryViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import static com.god.kahit.model.QuizGame.BUS;

public class CategoryView extends AppCompatActivity {
    private static final String LOG_TAG = AfterQuestionScorePageView.class.getSimpleName();
    private TextView sessionTypeTextView;
    private TextView categoryInfoTextView;
    private List<ImageButton> categoryButtons;
    private CategoryViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        model = ViewModelProviders.of(this).get(CategoryViewModel.class);

        sessionTypeTextView = findViewById(R.id.c_SessionType_textView);
        categoryInfoTextView = findViewById(R.id.categoryActivity_voteCategory_textView);

        categoryButtons = new ArrayList<>();
        categoryButtons.add((ImageButton) findViewById(R.id.cButton1));
        categoryButtons.add((ImageButton) findViewById(R.id.cButton2));
        categoryButtons.add((ImageButton) findViewById(R.id.cButton3));
        categoryButtons.add((ImageButton) findViewById(R.id.cButton4));
    }

    private void addPicturesToButton() {
        for (int i = 0; i < categoryButtons.size(); i++) {
            int resId = getResources().getIdentifier(model.getCategories()[i].toString() + "icon", "drawable", getApplicationInfo().packageName);
            categoryButtons.get(i).setImageResource(resId);
        }
    }

    public void onCategoryClick(View view) {
        int i = 0;
        if (view instanceof ImageButton) {
            i = categoryButtons.indexOf(view);
        }

        if (model.isHotSwap()) {
            model.setCategory(i);
            launchQuestionClass();
        } else {
            model.voteCategory(i);
        }
    }

    private void resetCategoryButtons() {
        for (ImageButton categoryButton : categoryButtons) {
            categoryButton.setBackgroundTintList(null);
            categoryButton.setEnabled(true);
        }
    }

    private void greyOutCategoryButtons() {
        for (ImageButton categoryButton : categoryButtons) {
            categoryButton.setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.light_grey, null)));
            categoryButton.setEnabled(false);
        }
    }

    private void colorSelectedCategoryButton(String categoryId) {
        int buttonIndex = model.getCategoryIndex(categoryId);
        categoryButtons.get(buttonIndex).setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.blue, null)));
    }

    private void colorResultCategoryButton(String categoryId) {
        int buttonIndex = model.getCategoryIndex(categoryId);
        categoryButtons.get(buttonIndex).setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.orange, null)));
    }

    public void launchQuestionClass() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, QuestionView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (model.isHotSwap()) {
            model.generateCategories();
        } else {
            model.updateCategories();
            model.resetVote();
        }
        if (!BUS.isRegistered(this)) {
            BUS.register(this);
        }

        if (model.isHotSwap()) {
            sessionTypeTextView.setText("Hotswap mode");
        } else {
            sessionTypeTextView.setText(String.format("%s - id: '%s'", model.isHost() ? "Host" : "Client", model.getMyPlayerId()));
        }
        categoryInfoTextView.setText("Vote for next category");
        resetCategoryButtons();
        addPicturesToButton();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BUS.unregister(this);
    }

    @Subscribe
    public void onPlayerVotedCategoryEvent(PlayerVotedCategoryEvent event) {
        Log.d(LOG_TAG, "onPlayerVotedCategoryEvent: event triggered");

        if (model.isMe(event.getPlayer())) {
            Log.d(LOG_TAG, "onPlayerVotedCategoryEvent: was me! updating views");
            greyOutCategoryButtons();
            colorSelectedCategoryButton(event.getCategoryId());
        }
        if (model.isHost()) {
            model.onCategoryVoteEvent(event.getCategoryId());
        }
    }

    @Subscribe
    public void onPlayerCategoryVoteResultEvent(CategoryVoteResultEvent event) {
        Log.d(LOG_TAG, "onPlayerCategoryVoteResultEvent: event triggered, showing vote result.");
        greyOutCategoryButtons();
        colorResultCategoryButton(event.getCategoryId());
        int categoryIndex = model.getCategoryIndex(event.getCategoryId());
        categoryInfoTextView.setText(String.format("Next category is: '%s'", model.getCategories()[categoryIndex].toString()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                model.sendIsReady();
            }
        }, 2000);
    }

    @Subscribe
    public void onNewViewEvent(NewViewEvent event) {
        model.resetPlayersReady();
        Intent intent = new Intent(getApplicationContext(), event.getNewViewClass());
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onAllPlayersReadyEvent(AllPlayersReadyEvent event) {
//        countdownTextView.setText("All players ready!"); //todo show waiting for server etc
        if (model.isHost()) {
            Log.d(LOG_TAG, "onAllPlayersReadyEvent: event triggered, showing next view.");
            model.showNextView();
        }
    }

    @Subscribe
    public void onGameLostConnectionEvent(GameLostConnectionEvent event) {
        if (!model.isHost()) {
            Log.d(LOG_TAG, "onGameLostConnectionEvent: event triggered");

            Toast.makeText(getApplicationContext(), "Lost connection to game!",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ChooseGameView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Log.d(LOG_TAG, "onGameLostConnectionEvent: event triggered, but I am host - skipping");
        }
    }
}