package com.god.kahit.view;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Lottery;

import java.util.Collections;

/**
 * Lottery page that shows up players' name, selfie and the randomized item(Buff or Debuff) that they've got  the concept of "items" mean which Buffs or Debuffs that the player got  Debuffs are the items that player can send to other players and negatively effect them
 */
/** the concept of "items" mean which Buffs or Debuffs that the player got */
/** Debuffs are the items that player can send to other players and negatively effect them */

/** Buffs are the items that players positively can effect themselves */
public class LotteryClass extends AppCompatActivity {

    final private Handler handler = new Handler();
    Lottery lottery = new Lottery();
    int count = 0;
    int maxCount = 10;

    int rand ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_activity);

        populatePlayerImage();
        populatePlayerName();
        drawItem();
    }

    private void incCouunter(){
        count++;
    }

    private boolean isDone(){
        return count >= maxCount;
    }

    public void drawItem() {
        int i;
        /** Handler object is used to iterate some code/message in X ms. And here is used to iterate a random item */

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if ( !isDone()) {
                        incCouunter();
                        rand = lottery.getRandom().nextInt(lottery.getItems().size());
                        /** getting a random image id, which is the item */
                        int imgId = getImageId(rand);
                        populateBuffsDebuffs(imgId);
                        /** redo handler */

                        handler.postDelayed(this, lottery.getDelay());
                    }
                    else{
                        int imgId = getImageId(1);
                        populateBuffsDebuffs(imgId);
                    }
                }
            }, lottery.getDelay());

    }

    public int getImageId(int id) {
        return getResources().getIdentifier(lottery.getItems().get(id).getImageSource(), "drawable", getPackageName());
    }

    private void populateBuffsDebuffs(int imgId) {
        //todo
        lottery.getItemPlayerList().add((ImageView) findViewById(R.id.lPlayerItem0ImageView));
        lottery.getItemPlayerList().get(0).setImageResource(imgId);
        Collections.shuffle(lottery.getItemPlayerList());
        lottery.getItemPlayerList().add((ImageView) findViewById(R.id.lPlayerItem1ImageView));
        lottery.getItemPlayerList().get(1).setImageResource(imgId);
        Collections.shuffle(lottery.getItemPlayerList());
        lottery.getItemPlayerList().add((ImageView) findViewById(R.id.lPlayerItem2ImageView));
        lottery.getItemPlayerList().get(2).setImageResource(imgId);
        Collections.shuffle(lottery.getItemPlayerList());
        lottery.getItemPlayerList().add((ImageView) findViewById(R.id.lPlayerItem3ImageView));
        lottery.getItemPlayerList().get(3).setImageResource(imgId);
        Collections.shuffle(lottery.getItemPlayerList());
        lottery.getItemPlayerList().add((ImageView) findViewById(R.id.lPlayerItem4ImageView));
        lottery.getItemPlayerList().get(4).setImageResource(imgId);
        Collections.shuffle(lottery.getItemPlayerList());
        lottery.getItemPlayerList().add((ImageView) findViewById(R.id.lPlayerItem5ImageView));
        lottery.getItemPlayerList().get(5).setImageResource(imgId);
        Collections.shuffle(lottery.getItemPlayerList());
        lottery.getItemPlayerList().add((ImageView) findViewById(R.id.lPlayerItem6ImageView));
        lottery.getItemPlayerList().get(6).setImageResource(imgId);
        Collections.shuffle(lottery.getItemPlayerList());
        lottery.getItemPlayerList().add((ImageView) findViewById(R.id.lPlayerItem7ImageView));
        lottery.getItemPlayerList().get(7).setImageResource(imgId);
        Collections.shuffle(lottery.getItemPlayerList());
    }

    public void populatePlayerImage() {
        // todo (values from player's selfie)
        lottery.getImagePlayerList().add((ImageView) findViewById(R.id.lPlayerPic0ImageView));
        lottery.getImagePlayerList().get(0).setImageResource(R.drawable.test1);
        lottery.getImagePlayerList().add((ImageView) findViewById(R.id.lPlayerPic1ImageView));
        lottery.getImagePlayerList().get(1).setImageResource(R.drawable.test2);
        lottery.getImagePlayerList().add((ImageView) findViewById(R.id.lPlayerPic2ImageView));
        lottery.getImagePlayerList().get(2).setImageResource(R.drawable.test3);
        lottery.getImagePlayerList().add((ImageView) findViewById(R.id.lPlayerPic3ImageView));
        lottery.getImagePlayerList().get(3).setImageResource(R.drawable.test4);
        lottery.getImagePlayerList().add((ImageView) findViewById(R.id.lPlayerPic4ImageView));
        lottery.getImagePlayerList().get(4).setImageResource(R.drawable.test5);
        lottery.getImagePlayerList().add((ImageView) findViewById(R.id.lPlayerPic5ImageView));
        lottery.getImagePlayerList().get(5).setImageResource(R.drawable.test6);
        lottery.getImagePlayerList().add((ImageView) findViewById(R.id.lPlayerPic6ImageView));
        lottery.getImagePlayerList().get(6).setImageResource(R.drawable.test7);
        lottery.getImagePlayerList().add((ImageView) findViewById(R.id.lPlayerPic7ImageView));
        lottery.getImagePlayerList().get(7).setImageResource(R.drawable.test8);
    }

    public void populatePlayerName() {
        // todo (values from players name in GameObject (or teams?))
        lottery.getTextPlayerList().add((TextView) findViewById(R.id.lPlayerName0TextView));
        lottery.getTextPlayerList().get(0).setText("Oussama");
        lottery.getTextPlayerList().add((TextView) findViewById(R.id.lPlayerName1TextView));
        lottery.getTextPlayerList().get(1).setText("Mats");
        lottery.getTextPlayerList().add((TextView) findViewById(R.id.lPlayerName2TextView));
        lottery.getTextPlayerList().get(2).setText("Anas");
        lottery.getTextPlayerList().add((TextView) findViewById(R.id.lPlayerName3TextView));
        lottery.getTextPlayerList().get(3).setText("Jakob");
        lottery.getTextPlayerList().add((TextView) findViewById(R.id.lPlayerName4TextView));
        lottery.getTextPlayerList().get(4).setText("Johan");
        lottery.getTextPlayerList().add((TextView) findViewById(R.id.lPlayerName5TextView));
        lottery.getTextPlayerList().get(5).setText("Blah");
        lottery.getTextPlayerList().add((TextView) findViewById(R.id.lPlayerName6TextView));
        lottery.getTextPlayerList().get(6).setText("Bingo");
        lottery.getTextPlayerList().add((TextView) findViewById(R.id.lPlayerName7TextView));
        lottery.getTextPlayerList().get(7).setText("Bajs");

    }


}
