package com.god.kahit.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.god.kahit.R;
import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.model.Item;
import com.god.kahit.model.Player;
import com.god.kahit.viewModel.LotteryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * responsibility: Lottery page that shows up players' name and the randomized item(Buff or Debuff) that they get.
 * used-by: AfterQuestionScorePageView, LotteryView, Repository.
 *
 * @author Oussama Anadani, Jakob Ewerstrand
 */
public class LotteryView extends AppCompatActivity {
    private static final String LOG_TAG = LotteryView.class.getSimpleName();
    LotteryViewModel lotteryViewModel;
    ConstraintLayout constraintLayout;

    private List<TextView> textViewList;
    private List<ImageView> imageViewList;
    private List<ImageView> playerImageViews = new ArrayList<>();
    private List<TextView> playerNameTextViews = new ArrayList<>();

    private int count;

    private MutableLiveData<Map<Player, Item>> mapWinningsLiveData;
    private MutableLiveData<List<Item>> itemListLiveData;
    private MutableLiveData<List<Player>> playerListLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_activity);

        initInstanceVariables();
        getLifecycle().addObserver(lotteryViewModel);

        populateLayoutViewDynamically();
        displayLottery();
    }

    private void initInstanceVariables() {
        constraintLayout = findViewById(R.id.lotteryActivity);
        lotteryViewModel = ViewModelProviders.of(this).get(LotteryViewModel.class);
        mapWinningsLiveData = lotteryViewModel.getMapWinningsLiveData();
        itemListLiveData = lotteryViewModel.getItemListLiveData();
        playerListLiveData = lotteryViewModel.getPlayerListLiveData();
    }

    /**
     * Populates the Layout with playerNames and PlayerImages in a circle.
     */
    private void populateLayoutViewDynamically() {
        int angle;
        final int numOfPlayers = Objects.requireNonNull(playerListLiveData.getValue()).size();
        textViewList = setupPlayerTextViews();
        imageViewList = setupPlayerImageViews();
        for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
            angle = playerIndex * (360 / numOfPlayers);
            setUpPlayerViews(playerIndex, angle);
        }
    }

    /**
     * Sets the players name below the picture.
     *
     * @return list of players' names.
     */
    private List<TextView> setupPlayerTextViews() {
        for (int playerIndex = 0; playerIndex < Objects.requireNonNull(playerListLiveData.getValue()).size(); playerIndex++) {
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.textsize));
            textView.setText(Objects.requireNonNull(playerListLiveData.getValue()).get(playerIndex).getName());
            playerNameTextViews.add(textView);
        }
        return playerNameTextViews;
    }

    /**
     * Sets the players images.
     *
     * @return a list of images for players
     */
    private List<ImageView> setupPlayerImageViews() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.test5);
        final int numOfPlayers = Objects.requireNonNull(playerListLiveData.getValue()).size();

        for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(drawable);
            playerImageViews.add(imageView);
        }
        return playerImageViews;
    }

    /**
     * Setts up the images of players in a circular shape
     *
     * @param index player index.
     * @param angle the angle of the circle. In other words, the space between the players in a circular shape.
     */
    public void setUpPlayerViews(int index, int angle) {

        ConstraintLayout.LayoutParams layoutParams;
        layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.circleRadius = 400;
        layoutParams.circleConstraint = this.findViewById(R.id.centerPoint).getId();
        layoutParams.circleAngle = angle;
        imageViewList.get(index).setLayoutParams(layoutParams);
        constraintLayout.addView(imageViewList.get(index));
        int x = (int) imageViewList.get(index).getX();
        int y = (int) imageViewList.get(index).getY();
        setUpTextViewList(index, x, y);
    }

    /**
     * Setts up the names of players in a circular shape. This method gets its parameters from setupImageViewList method
     * and adds additional pixels to locate the names below the images.
     *
     * @param index             player index
     * @param xImageCoordinates x coordinate of the image
     * @param yImageCoordinates y coordinate of the image
     */
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
     * To check if the counter is done after maximum of amount of times.
     *
     * @return the state of the counter
     */
    private boolean isDone() {
        int MAX_IMAGE_SWAPS = 20;
        return count >= MAX_IMAGE_SWAPS;
    }


    /**
     * A method to get a random image id.
     *
     * @param id A random number to get a random image id
     * @return the randomized image
     */
    public int getImageId(int id) {
        Map<String, String> map = ItemDataLoaderRealtime.getItemImageNameMap();
        String string = map.get(Objects.requireNonNull(itemListLiveData.getValue()).get(id).getName());
        return getResources().getIdentifier(string, "drawable", getPackageName());
    }

    /**
     * Displays the lottery using Handler object, with delay X ms.
     */
    public void displayLottery() {
        final Handler handler = new Handler();
        final int delay = 200;
        count = 0;
        final int numOfPlayers = Objects.requireNonNull(playerListLiveData.getValue()).size();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                //Checks if the lottery is done after maxCount times.
                if (!isDone() || mapWinningsLiveData.getValue() == null) {
                    count++;
                    for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
                        setRandomItemImage(playerIndex);
                    }
                    //When each player has been updated with a random image, run this thread again after set delay.
                    handler.postDelayed(this, delay);

                } else { // When lottery has finished.
                    for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
                        setWonItemImage(playerIndex);
                        setWonItemName(playerIndex);
                    }
                    for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
                        imageAnimation(playerIndex);
                    }
                    //Again small delay before next View is launched.
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            launchCategoryView();
                        }
                    }, 3000);
                }
            }
        }, delay);
    }

    /**
     * Method that sets a random items image on the ImageViews.
     *
     * @param index current image of player
     */
    private void setRandomItemImage(int index) {
        Random random = new Random();
        int randomNumber = random.nextInt(Objects.requireNonNull(itemListLiveData.getValue()).size());
        int imgId = getImageId(randomNumber);
        playerImageViews.get(index).setImageResource(imgId);
    }

    /**
     * this method sets the image of the won item to the players
     *
     * @param index won item image index
     */
    private void setWonItemImage(int index) {
        Map<String, String> map = ItemDataLoaderRealtime.getItemImageNameMap();
        String string = map.get(Objects.requireNonNull(
                Objects.requireNonNull(mapWinningsLiveData.getValue()).get(
                        Objects.requireNonNull(playerListLiveData.getValue()).get(index))).getName());
        int i = getResources().getIdentifier(string, "drawable", getPackageName());
        playerImageViews.get(index).setImageResource(i);
    }

    /**
     * Set the won item name next to the players name.
     *
     * @param index player index
     */
    private void setWonItemName(int index) {
        String itemName = "Not found";
        if (mapWinningsLiveData != null) {
            Map<Player, Item> winnings = mapWinningsLiveData.getValue();
            if (winnings != null) {
                List<Player> playerList = playerListLiveData.getValue();
                if (playerList != null) {
                    Player player = playerList.get(index);
                    Item item = winnings.get(player);
                    if (item != null) {
                        itemName = item.getName();
                    } else {
                        Log.d(LOG_TAG, "setWonItemName: item == null, unable to set wonItemName");
                    }
                } else {
                    Log.d(LOG_TAG, "setWonItemName: playerList == null, unable to set wonItemName");
                }
            } else {
                Log.d(LOG_TAG, "setWonItemName: winnings == null, unable to set wonItemName");
            }
        } else {
            Log.d(LOG_TAG, "setWonItemName: mapWinningsLiveData == null, unable to set wonItemName");
        }

//        String itemName = mapWinningsLiveData.getValue().get(playerListLiveData.getValue().get(index)).getName();
        playerNameTextViews.get(index).setText(itemName);
    }

    /**
     * Animates the images when lottery has finished
     *
     * @param index player index
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void imageAnimation(int index) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(playerImageViews.get(index), "scaleX", 1.6f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(playerImageViews.get(index), "scaleY", 1.6f);
        scaleDownX.setDuration(200);
        scaleDownY.setDuration(200);

        AnimatorSet scaleDown1 = new AnimatorSet();
        scaleDown1.play(scaleDownX).with(scaleDownY);
        scaleDown1.start();
        scaleDown1.setDuration(5000);
        scaleDown1.reverse();
    }

    /**
     * Launching the categories page when lottery has finished
     */
    public void launchCategoryView() {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, CategoryView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}