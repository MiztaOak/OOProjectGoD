package com.god.kahit.view;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.ViewModel.LotteryViewModel;
import com.god.kahit.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Lottery page that shows up players' name, selfie and the randomized item(Buff or Debuff) that they get *
 * the concept of "items" means which Buffs or Debuffs that the player gets *
 * Debuffs are the items that player can send to other players and negatively affect them *
 * Buffs are the items that players positively can affect themselves *
 */
public class LotteryView extends AppCompatActivity {

    LotteryViewModel lotteryViewModel;

    ConstraintLayout constraintLayout;
    // View view = findViewById(R.id.lotteryItem);
    ConstraintLayout.LayoutParams layoutParams;
    List<TextView> textViewList;
    List<ImageView> imageViewList;
    private Handler handler = new Handler();
    private int count = 0;
    private int maxCount = 10;
    private int randomNumber;
    private ImageView icon;
    private List<ImageView> imageTest = new ArrayList<>();

    private MutableLiveData<Map<Integer, String>> playerMap;
    private MutableLiveData<Map<Integer, Item>> lotteryItemMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_layout);

        lotteryViewModel = ViewModelProviders.of(this).get(LotteryViewModel.class);
        constraintLayout = findViewById(R.id.constraintLayoutId);



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
        populateLayoutViewDynamically();
        displayLottery();

        //TODO
        //setPositionOfMapItems();
        //populatePlayerImage();
        // populatePlayerName();

    }


    /**
     * Populates the Layout with playerNames and PlayerImages in a circle.
     */
    private void populateLayoutViewDynamically() {

        int childId = getCenterChildId();
        int angle;
        int numOfPlayers = 8;
        textViewList = setupPlayerTextViews();
        imageViewList = setupPlayerImageViews();
        for (int i = 0; i < numOfPlayers; i++) {
            angle = i * (360 / numOfPlayers);
            setUpImageViewList(i, angle, childId);

        }
    }

    public void setUpImageViewList(int i, int angle, int childId) {
        layoutParams = new ConstraintLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        layoutParams.width = 150;
        layoutParams.height = 200;
        layoutParams.circleRadius = 400;
        layoutParams.circleConstraint = childId;
        layoutParams.circleAngle = angle;
        imageViewList.get(i).setLayoutParams(layoutParams);
        constraintLayout.addView(imageViewList.get(i));
        int x = (int) imageViewList.get(i).getX();
        int y = (int) imageViewList.get(i).getY();
        setUpTextViewList(i, x, y);
    }


    public void setUpTextViewList(int i, int xPicCoordinates, int yPicCoordinates) {
        layoutParams = (ConstraintLayout.LayoutParams) imageViewList.get(i).getLayoutParams();
        textViewList.get(i).setX(xPicCoordinates + 10);
        textViewList.get(i).setY(yPicCoordinates + 200);
        textViewList.get(i).setLayoutParams(layoutParams);
        constraintLayout.addView(textViewList.get(i));
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
        List<TextView> playerNameTxtViews = new ArrayList<>();
        for (int i = 0; i < 8; i++) { //TODO Size of list with player names
            TextView textView = new TextView(this);
            if (playerMap.getValue() != null) {
                textView.setText(playerMap.getValue().get(i));
                //textView.setId(i);
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

       // List<ImageView> playerImageViews = new ArrayList<>();
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.test5); //TODO more pictures.
        for (int i = 0; i < 8; i++) { //TODO Size of list with players
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(drawable);
            imageView.setId(i);
            imageTest.add(imageView);
        }
        return imageTest;
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


    public void displayLottery() {
       final int delay = 200;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if (!isDone()) {
                    incCounter();

                    for (int i = 0; i < 8; i++) {  //TODO Size of list with player names
                        randomNumber = lotteryViewModel.getRandom().nextInt(lotteryItemMap.getValue().size()-1);
                        int imgId = getImageId(randomNumber);
                        imageTest.get(i).setImageResource(imgId);
                    }
                    //When each player has been updated, run this thread again after set delay
                    handler.postDelayed(this, delay);
                }
               else {

                    //Create pre-calculated won item image for each player
                    for (int i = 0; i < 8; i++) {
                      //  int imgId = getImageId(getWonItemPlayer(1));  // todo instead : getWonItemPlayer(1));

                      //  lotteryViewModel.getLottery().getPlayers().get(i).setWonItem(lotteryViewModel.getWonItem(lotteryViewModel.getLottery().getBuffDebuffItems()));

                    }
                }
            }
        }, delay);
    }


    private void populateViewWithPlayers() {
        int numOfPlayers;
        if (playerMap.getValue() != null) {
            numOfPlayers = playerMap.getValue().size();
        }
    }

    /*public void addItemsToMap(int indexOfPlayer) { // adding players

        switch (indexOfPlayer) {
            default:
                //todo add player name and wonItem
                break;
            case (0):
                //todo add player name and wonItem
                Objects.requireNonNull(lottery.getMap().get(indexOfPlayer)).getPlayerImage().setImageResource(R.drawable.test1);
                break;
            case (1):
                //todo add player name and wonItem
                Objects.requireNonNull(lottery.getMap().get(indexOfPlayer)).getPlayerImage().setImageResource(R.drawable.test2);
                break;
            case (2):
                //todo add player name and wonItem
                Objects.requireNonNull(lottery.getMap().get(indexOfPlayer)).getPlayerImage().setImageResource(R.drawable.test4);
                break;
            case (3):
                //todo add player name and wonItem
                Objects.requireNonNull(lottery.getMap().get(indexOfPlayer)).getPlayerImage().setImageResource(R.drawable.test5);
                break;
            case (4):
                //todo add player name and wonItem
                Objects.requireNonNull(lottery.getMap().get(indexOfPlayer)).getPlayerImage().setImageResource(R.drawable.test6);
                break;
            case (5):
                //todo add player name and wonItem
                Objects.requireNonNull(lottery.getMap().get(indexOfPlayer)).getPlayerImage().setImageResource(R.drawable.test7);
                break;
            case (6):
                //todo add player name and wonItem
                Objects.requireNonNull(lottery.getMap().get(indexOfPlayer)).getPlayerImage().setImageResource(R.drawable.test8);
                break;
            case (7):
                //todo add player name and wonItem
                Objects.requireNonNull(lottery.getMap().get(indexOfPlayer)).getPlayerImage().setImageResource(R.drawable.test9);
                break;

        }


    }*/

   /* public void setPositionOfMapItems() {
        int numOfMapItems = lottery.getMap().size(); //number of players
        switch (numOfMapItems) {
            default:
                //todo no Players Found!
                break;
            case (1):
                //todo 1 Players Found

                break;
            case (2):
                //todo 2 Players Found
                break;
            case (3):
                //todo 3 Players Found
                break;
            case (4):
                //todo 4 Players Found
                break;
            case (5):
                //todo 5 Players Found
                break;
            case (6):
                //todo 6 Players Found
                break;
            case (7):
                //todo 7 Players Found
                break;
            case (8):
                //todo 8 Players Found
                RelativeLayout r1 = findViewById(R.id.lotteryLayout);
                RelativeLayout map = findViewById(R.id.lotteryPlayerMap);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
                params.leftMargin = 50;
                params.topMargin = 60;
                r1.addView(map, params);

                break;

        }

    }*/


    public void populateItemAnimation() {
        Animation popup;

    }


}
