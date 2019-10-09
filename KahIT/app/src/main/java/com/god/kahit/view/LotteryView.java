package com.god.kahit.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.ViewModel.LotteryViewModel;
import com.god.kahit.model.Item;
import com.god.kahit.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Lottery page that shows up players' name and the randomized item(Buff or Debuff) that they get *
 * the concept of "items" means which Buffs or Debuffs that the player gets *
 * Debuffs are the items that player can send to other players and negatively affect them *
 * Buffs are the items that players positively can affect themselves *
 */
public class LotteryView extends AppCompatActivity {
    LotteryViewModel lotteryViewModel;
    ConstraintLayout constraintLayout;

    List<TextView> textViewList;
    List<ImageView> imageViewList;
    List<ImageView> playerImageViews = new ArrayList<>();
    List<TextView> playerNameTxtViews = new ArrayList<>();

    private Random random;
    private int count = 0;
    private int maxCount = 10;

    private MutableLiveData<Map<Player, Item>> mapWinningsLiveData;
    private MutableLiveData<List<Item>> itemListLiveData;
    private MutableLiveData<List<Player>> playerListLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_activity);

        constraintLayout = findViewById(R.id.lotteryActivity);


        lotteryViewModel = ViewModelProviders.of(this).get(LotteryViewModel.class);
        mapWinningsLiveData = lotteryViewModel.getMapWinningsLiveData();
        itemListLiveData = lotteryViewModel.getItemListLiveData();
        playerListLiveData = lotteryViewModel.getPlayerListLiveData();

        lotteryViewModel.getMapWinningsLiveData().observe(this, new Observer<Map<Player, Item>>() {
            @Override
            public void onChanged(Map<Player, Item> playerItemMap) {

            }
        });

        lotteryViewModel.getItemListLiveData().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {

            }
        });

        lotteryViewModel.getPlayerListLiveData().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> playerList) {

            }
        });
        initLottery();

    }

    private void initLottery() {
        populateLayoutViewDynamically();
        displayLottery();
    }


    /**
     * Populates the Layout with playerNames and PlayerImages in a circle.
     */
    private void populateLayoutViewDynamically() {
        int childId = getCenterChildId();
        int angle;
        final int numOfPlayers = Objects.requireNonNull(playerListLiveData.getValue()).size();
        textViewList = setupPlayerTextViews();
        imageViewList = setupPlayerImageViews();
        for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
            angle = playerIndex * (360 / numOfPlayers);
            setUpImageViewList(playerIndex, angle, childId);
        }
    }

    /**
     * Gets the id for the invisible ImageView in the center of the layout.
     *
     * @return id as an int.
     */
    private int getCenterChildId() {
        int val = 0;
        int count = constraintLayout.getChildCount();
        for (int k = 0; k <= count; k++) {
            View v = constraintLayout.getChildAt(k);
            if (v instanceof ImageView) {
                return v.getId();
            }
        }
        return val;
    }

    /**
     * Initiates all TextViews.
     *
     * @return list of TextViews.
     */
    private List<TextView> setupPlayerTextViews() {

        for (int playerIndex = 0; playerIndex < Objects.requireNonNull(mapWinningsLiveData.getValue()).size(); playerIndex++) {
            TextView textView = new TextView(this);
            textView.setText(playerListLiveData.getValue().get(playerIndex).getName());
            playerNameTxtViews.add(textView);
        }
        return playerNameTxtViews;
    }


    /**
     * Initiates all imageViewList.
     *
     * @return
     */
    private List<ImageView> setupPlayerImageViews() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.test5); //TODO later, more pictures
        final int numOfPlayers = Objects.requireNonNull(playerListLiveData.getValue()).size();

        for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(drawable);
            imageView.setId(playerIndex);
            playerImageViews.add(imageView);
        }
        return playerImageViews;
    }

    public void setUpImageViewList(int index, int angle, int childId) {
        ConstraintLayout.LayoutParams layoutParams;
        layoutParams = new ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.width = 150;
        layoutParams.height = 200;
        layoutParams.circleRadius = 400;
        layoutParams.circleConstraint = childId;
        layoutParams.circleAngle = angle;
        imageViewList.get(index).setLayoutParams(layoutParams);
        constraintLayout.addView(imageViewList.get(index));
        int x = (int) imageViewList.get(index).getX();
        int y = (int) imageViewList.get(index).getY();
        setUpTextViewList(index, x, y);
    }


    public void setUpTextViewList(int index, int xImageCoordinates, int yImageCoordinates) {
        ConstraintLayout.LayoutParams layoutParams;
        // getting same layoutParams of imageViewList and using it for textViewList
        layoutParams = (ConstraintLayout.LayoutParams) imageViewList.get(index).getLayoutParams();
        // adding extra coordinates to show the textName below the image
        textViewList.get(index).setX(xImageCoordinates + 10);
        textViewList.get(index).setY(yImageCoordinates + 200);
        textViewList.get(index).setLayoutParams(layoutParams);
        constraintLayout.addView(textViewList.get(index));
    }

    private void incCounter() {
        count++;
    }

    private boolean isDone() {
        return count >= maxCount;
    }

    public int getImageId(int id) {
        return getResources().getIdentifier(Objects.requireNonNull(
                itemListLiveData.getValue()).get(id).getImageSource(),
                "drawable",
                getPackageName());
    }

    /**
     * Displays the lottery using Handler object, with delay X ms
     */
    public void displayLottery() {
        final Handler handler = new Handler();
        final int delay = 200;
        final int numOfPlayers = Objects.requireNonNull(playerListLiveData.getValue()).size();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O) //todo Hmm..? Results in crash with api 24?
            @Override
            public void run() {
                // checks if the lottery is done after maxCount times
                if (!isDone()) {
                    incCounter();
                    for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
                        setItemImage(playerIndex);
                    }
                    // When each player has been updated with a random image, run this thread again after set delay
                    handler.postDelayed(this, delay);

                } else { // When lottery has finished
                    for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
                        setWonItemImage(playerIndex);
                        try {
                            imageAnimation(playerIndex);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, delay);
    }

    /**
     *
     * @param index
     */
    private void setItemImage(int index) {
        random = new Random();
        // Gets a random number from the list size
        int randomNumber = random.nextInt(Objects.requireNonNull(itemListLiveData.getValue()).size());
        // Getting an imageId(or image) with a random number
        int imgId = getImageId(randomNumber);
        // allocates the image to the user "index"
        playerImageViews.get(index).setImageResource(imgId);
    }

    /**
     *
     * @param index
     */
    private void setWonItemImage(int index) {
        Item item = mapWinningsLiveData.getValue().get(playerListLiveData.getValue().get(index));
        int imgId = getResources().getIdentifier(Objects.requireNonNull(item).getImageSource(), "drawable", getPackageName());
        playerImageViews.get(index).setImageResource(imgId);
    }

    /**
     * Animates the images after lottery has finished
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void imageAnimation(int index)
            throws InterruptedException {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(playerImageViews.get(index), "scaleX", 1.6f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(playerImageViews.get(index), "scaleY", 1.6f);
        scaleDownX.setDuration(200);
        scaleDownY.setDuration(200);

        AnimatorSet scaleDown1 = new AnimatorSet();
        scaleDown1.play(scaleDownX).with(scaleDownY);
        scaleDown1.start();
        scaleDown1.setDuration(1000);
        scaleDown1.reverse();

    }


}
