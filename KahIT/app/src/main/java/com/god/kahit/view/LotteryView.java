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
import com.god.kahit.viewModel.LotteryViewModel;
import com.god.kahit.model.Item;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Lottery page that shows up players' name, selfie and the randomized item(Buff or Debuff) that they get *
 * the concept of "items" means which Buffs or Debuffs that the player gets *
 * Debuffs are the items that player can send to other players and negatively effect them *
 * Buffs are the items that players positively can effect themselves *
 */
public class LotteryView extends AppCompatActivity {

    LotteryViewModel lotteryViewModel;

    ConstraintLayout constraintLayout;
    ConstraintLayout.LayoutParams layoutParams;

    private Handler handler;
    private int count = 0;
    private int maxCount = 10;
    private int randomNumber;

    List<TextView> textViewList;
    List<ImageView> imageViewList;

    private MutableLiveData<Map<Integer, String>> playerMap;
    private MutableLiveData<Map<Integer, Item>> lotteryItemMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_layout);

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

        //TODO
        //populatePlayerImage();
        // populatePlayerName();
        // drawItem();

        populateLayoutViewDynamically();
        //setPositionOfMapItems();

    }


    /**
     * Populates the Layout with playerNames and PlayerImages in a circle.
     */
    private void populateLayoutViewDynamically() {
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutId);

        textViewList = setupPlayerTextViews();
        imageViewList = setupPlayerImageViews();

        int childId = getCenterChildId();

        int i;
        int angle;
        int iNumberOfButtons = 8; //TODO Size of list with player names, change to something else < 8 for demonstration.

        for(i = 0; i < iNumberOfButtons; i++) {
            angle = i * (360/iNumberOfButtons);

            layoutParams = new ConstraintLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );

            layoutParams.width = 200;
            layoutParams.height = 200;
            layoutParams.circleRadius = 400; //radius of circle
            layoutParams.circleConstraint = childId;
            layoutParams.circleAngle = angle;

            imageViewList.get(i).setLayoutParams(layoutParams);
            layoutParams.topToBottom = i;

            textViewList.get(i).setLayoutParams(layoutParams);


            constraintLayout.addView(imageViewList.get(i));
            constraintLayout.addView(textViewList.get(i));

            //layout_toBottomOf
        }
    }

    /**
     * Gets the id for the invisible ImageView in the center of the layout.
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
     * @return list of TextViews.
     */
    private List<TextView> setupPlayerTextViews() {
        List<TextView> playerNameTxtViews = new ArrayList<>();

        int i;

        for(i = 0; i < 8; i++) { //TODO Size of list with player names
            TextView textView = new TextView(this);
            if(playerMap.getValue() != null) {
                textView.setText(playerMap.getValue().get(i));
                textView.setId(i);
                playerNameTxtViews.add(textView);
            }
        }
        return playerNameTxtViews;
    }


    /**
     * Initiates all imageViewList.
     * @return
     */
    private List<ImageView> setupPlayerImageViews() {

        List<ImageView> playerImageViews = new ArrayList<>();
        int i;
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.player1); //TODO more pictures.

        for(i = 0; i < 8; i++) { //TODO Size of list with player names
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(drawable);
            imageView.setId(i);

            playerImageViews.add(imageView);
        }
        return playerImageViews;
    }

    private void populateViewWithPlayers() {
        int numOfPlayers;
        if (playerMap.getValue() != null) {
            numOfPlayers = playerMap.getValue().size();
        }


        //RelativeLayout r1 = findViewById(R.id.lotteryLayout);
        //RelativeLayout map = findViewById(R.id.lotteryPlayerMap);
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
       // params.leftMargin = 50;
       // params.topMargin = 60;
       // r1.addView(map, params);


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

    private void incCounter() {
        count++;
    }

    private boolean isDone() {
        return count >= maxCount;
    }

    public void drawItem() {



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isDone()) {
                    incCounter();

                    for (int i = 0; i < 8; i++) {  //TODO Size of list with player names
                        randomNumber = lotteryViewModel.getRandom().nextInt(lotteryItemMap.getValue().size());
                        int imgId = getImageId(randomNumber);

                    }

                    //When each player has been updated, run this thread again after set delay
                    handler.postDelayed(this, 500);
                } else {
                    //Create pre-calculated won item image for each player
                    for (int i = 0; i < 8; i++) {
                        int imgId = getImageId(i);  // todo instead : getWonItemPlayer(1));
                        // populateBuffsDebuffs(i, imgId);
                    }
                }
            }
        }, 500);

    }

    public int getImageId(int id) {
        if (lotteryItemMap.getValue() != null) {
            return getResources().getIdentifier(lotteryItemMap.getValue().get(id).getImageSource(), "drawable", getPackageName());
        } else {
            return 0;
        }
    }


    public void populateItemAnimation() {
        Animation popup;

    }


}
