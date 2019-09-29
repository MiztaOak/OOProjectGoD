package com.god.kahit.view;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.god.kahit.R;
import com.god.kahit.model.Lottery;
import com.god.kahit.model.lotteryItem;

import java.util.Objects;
import java.util.Random;

/**
 * Lottery page that shows up players' name, selfie and the randomized item(Buff or Debuff) that they get *
 * the concept of "items" means which Buffs or Debuffs that the player gets *
 * Debuffs are the items that player can send to other players and negatively effect them *
 * Buffs are the items that players positively can effect themselves *
 */
public class LotteryClass extends AppCompatActivity {

    private final int delay = 500;
    private final Handler handler = new Handler();
    private int count = 0;
    private int maxCount = 10;
    private int rand;
    private Lottery lottery = new Lottery();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_activity);


        // populatePlayerImage();
        // populatePlayerName();
        // drawItem();
        for (int i = 0; i < 8; i++) {
            createLotteryMapItems(i);
        }
        setPositionOfMapItems();
    }


    public void createLotteryMapItems(int i) {
        lottery.getMap().put(i, new lotteryItem());
        addItemsToMap(i);
    }


    public void addItemsToMap(int indexOfPlayer) { // adding players

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
    }

    public void setPositionOfMapItems() {
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
                RelativeLayout r1 = findViewById(R.id.lotteryActivity);
                RelativeLayout map = findViewById(R.id.lotteryItem);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
                params.leftMargin = 50;
                params.topMargin = 60;
                r1.addView(map, params);

                break;

        }

    }

    private void incCounter() {
        count++;
    }

    private boolean isDone() {
        return count >= maxCount;
    }

    public void drawItem() {
        /** Handler object is used to iterate some code/message in X ms. And here is used to iterate a random item */

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isDone()) {
                    incCounter();

                    for (int i = 0; i < 8; i++) {  //Create random item image for each player
                        rand = random.nextInt(lottery.getBuffDebuffItems().size());
                        int imgId = getImageId(rand); //getting a random image id, which is the item
                        //  populateBuffsDebuffs(i, imgId);
                    }

                    //When each player has been updated, run this thread again after set delay
                    handler.postDelayed(this, delay);
                } else {
                    //Create pre-calculated won item image for each player
                    for (int i = 0; i < 8; i++) {
                        int imgId = getImageId(i);  // todo instead : getWonItemPlayer(1));
                        // populateBuffsDebuffs(i, imgId);
                    }
                }
            }
        }, delay);

    }

    public int getImageId(int id) {
        return getResources().getIdentifier(lottery.getBuffDebuffItems().get(id).getImageSource(), "drawable", getPackageName());
    }


    public void populateItemAnimation() {
        Animation popup;

    }


}
