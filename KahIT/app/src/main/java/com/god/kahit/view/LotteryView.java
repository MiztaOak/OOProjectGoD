package com.god.kahit.view;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.ViewModel.LotteryViewModel;
import com.god.kahit.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Lottery page that shows up players' name and the randomized item(Buff or Debuff) that they get *
 * the concept of "items" means which Buffs or Debuffs that the player gets *
 * Debuffs are the items that player can send to other players and negatively affect them *
 * Buffs are the items that players positively can affect themselves *
 */
public class LotteryView extends AppCompatActivity {

    LotteryViewModel lotteryViewModel = new LotteryViewModel();
    ConstraintLayout constraintLayout;

    List<TextView> textViewList;
    List<ImageView> imageViewList;
    List<ImageView> playerImageViews = new ArrayList<>();
    List<TextView> playerNameTxtViews = new ArrayList<>();

    int numOfPlayers = 8;
    private int count = 0;
    private int maxCount = 10;

    private MutableLiveData<Map<Integer, String>> playerMap;
    private MutableLiveData<Map<Integer, Item>> lotteryItemMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_activity);

        constraintLayout = findViewById(R.id.lotteryActivity);
        initLottery();
    }


    private void initLottery() {
        initLiveData();
        populateLayoutViewDynamically();
        displayLottery();
    }

    public void initLiveData() {
        lotteryViewModel = ViewModelProviders.of(this).get(LotteryViewModel.class);
        playerMap = lotteryViewModel.getPlayerMap();
        lotteryViewModel.getPlayerMap().observe(this, new Observer<Map<Integer, String>>() {
            @Override
            public void onChanged(@Nullable Map<Integer, String> integerStringMap) {
                //TODO
            }
        });
        lotteryItemMap = lotteryViewModel.getLotteryItemMap();
        lotteryViewModel.getLotteryItemMap().observe(this, new Observer<Map<Integer, Item>>() {
            @Override
            public void onChanged(@Nullable Map<Integer, Item> integerBuyableItemMap) {




                //TODO
            }
        });
    }


    /**
     * Populates the Layout with playerNames and PlayerImages in a circle.
     */
    private void populateLayoutViewDynamically() {
        int childId = getCenterChildId();
        int angle;
        textViewList = setupPlayerTextViews();
        imageViewList = setupPlayerImageViews();
        for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
            angle = playerIndex * (360 / numOfPlayers);
            setUpImageViewList(playerIndex, angle, childId);
        }
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

        for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
            TextView textView = new TextView(this);
            if (playerMap.getValue() != null) {
                textView.setText(playerMap.getValue().get(playerIndex));
                playerNameTxtViews.add(textView);
            }
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
        for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(drawable);
            imageView.setId(playerIndex);
            playerImageViews.add(imageView);
        }
        return playerImageViews;
    }


    private void incCounter() {
        count++;
    }

    private boolean isDone() {
        return count >= maxCount;
    }

    public int getImageId(int id) {
        if (lotteryItemMap.getValue() != null) {
            return getResources().getIdentifier(lotteryItemMap.getValue().get(id).getImageSource(), "drawable", getPackageName());
        } else {
            return 0;
        }
    }

    /**
     * Displays the lottery using Handler object, with delay X ms
     */
    public void displayLottery() {
        final Handler handler = new Handler();
        final int delay = 200;
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                // checks if the lottery is done after maxCount times
                if (!isDone()) {
                    incCounter();
                    for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {

                        int itemId = getPlayerImageItem(playerIndex);
                        setWonItem(itemId, playerIndex);
                    }
                    // When each player has been updated with a random image, run this thread again after set delay
                    handler.postDelayed(this, delay);

                } else { // When lottery has finished

                    //Create pre-calculated won item image for each player
                    for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
                        try {
                            imageAnimation(playerIndex);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                /*For test
                *  for (int i = 0; i < 8 ; i++){
                    System.out.println("Player" + i + " "  + lotteryViewModel.getLottery().getPlayers().get(i).getWonItemName() );
                }*/
            }
        }, delay);

    }

    public int getPlayerImageItem(int index) {
        Random rand = new Random();
        // Gets a random number from the list size
        int randomNumber = rand.nextInt((Objects.requireNonNull(lotteryViewModel.getLotteryItemMap().getValue()).size()));
        // Getting an imageId(or image) with a random number
        int imgId = getImageId(randomNumber);
        // allocates the image to the user "index"
        playerImageViews.get(index).setImageResource(imgId);
        return randomNumber;
    }

    private void setWonItem(int itemId, int index) {
        lotteryViewModel.setWonItem((Objects.requireNonNull(lotteryViewModel.getLotteryItemMap().getValue()).get(itemId)), index);
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
