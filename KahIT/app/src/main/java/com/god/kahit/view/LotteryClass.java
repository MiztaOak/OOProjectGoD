package com.god.kahit.view;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.ItemFactory;
import com.god.kahit.model.Lottery;
import com.god.kahit.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LotteryClass extends AppCompatActivity {

    /***/
    Random random = new Random();

    private Lottery lottery;

    private List<Player> lotteryPlayers;

    private List<ImageView> imagePlayerList = new ArrayList<>();
    private List<TextView> textPlayerList= new ArrayList<>();
    private List<ImageView> itemPlayerList = new ArrayList<>();

    final Handler handler = new Handler();
    public boolean active = false;

    public LotteryClass (){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_activity);


        Button drawBtn = (Button) findViewById(R.id.lDrawButton);
        drawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active = false;
                populatePlayerImage();
                populatePlayerName();
                drawItem();
              //  populateBuffsDebuffs();

            }
        });

    }



    public void drawItem() {
        while(active) {
            final int rand = random.nextInt(ItemFactory.getItems().length); // todo double check if its right
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // todo something after 0.25s = 250ms
                    // todo show random items from String [][]
                  String randItem = ItemFactory.getCertainItem(rand,rand); // todo double check if its right
                  // todo get pic of the item //ask Anas
                    
                }
            }, 250); //dragningen sker 4 ggr sekunden + den tiden det tar för koden att köra.
        }
    }

    private void populateBuffsDebuffs() {
        //todo
        itemPlayerList.add((ImageView) findViewById(R.id.lPlayerItem0ImageView));
        // itemPlayerList.get(0).setImageResource(lotteryPool.get(0).getImageSource());
        itemPlayerList.add((ImageView) findViewById(R.id.lPlayerItem1ImageView));
        itemPlayerList.add((ImageView) findViewById(R.id.lPlayerItem2ImageView));
        itemPlayerList.add((ImageView) findViewById(R.id.lPlayerItem3ImageView));
        itemPlayerList.add((ImageView) findViewById(R.id.lPlayerItem4ImageView));
        itemPlayerList.add((ImageView) findViewById(R.id.lPlayerItem5ImageView));
        itemPlayerList.add((ImageView) findViewById(R.id.lPlayerItem6ImageView));
        itemPlayerList.add((ImageView) findViewById(R.id.lPlayerItem7ImageView));

    }

    public void populatePlayerImage() {
        // todo (values from player's selfie)
        imagePlayerList.add((ImageView) findViewById(R.id.lPlayerPic0ImageView));
        imagePlayerList.get(0).setImageResource(R.drawable.test1);
        imagePlayerList.add((ImageView) findViewById(R.id.lPlayerPic1ImageView));
        imagePlayerList.get(1).setImageResource(R.drawable.test2);
        imagePlayerList.add((ImageView) findViewById(R.id.lPlayerPic2ImageView));
        imagePlayerList.get(2).setImageResource(R.drawable.test3);
        imagePlayerList.add((ImageView) findViewById(R.id.lPlayerPic3ImageView));
        imagePlayerList.get(3).setImageResource(R.drawable.test4);
        imagePlayerList.add((ImageView) findViewById(R.id.lPlayerPic4ImageView));
        imagePlayerList.get(4).setImageResource(R.drawable.test5);
        imagePlayerList.add((ImageView) findViewById(R.id.lPlayerPic5ImageView));
        imagePlayerList.get(5).setImageResource(R.drawable.test6);
        imagePlayerList.add((ImageView) findViewById(R.id.lPlayerPic6ImageView));
        imagePlayerList.get(6).setImageResource(R.drawable.test7);
        imagePlayerList.add((ImageView) findViewById(R.id.lPlayerPic7ImageView));
        imagePlayerList.get(7).setImageResource(R.drawable.test8);
    }

    public void populatePlayerName() {
        // todo (values from players name in GameObject (or teams?))
        textPlayerList.add( (TextView)findViewById(R.id.lPlayerName0TextView));
        textPlayerList.get(0).setText("Oussama");
        textPlayerList.add( (TextView)findViewById(R.id.lPlayerName1TextView));
        textPlayerList.get(1).setText("Mats");
        textPlayerList.add( (TextView)findViewById(R.id.lPlayerName2TextView));
        textPlayerList.get(2).setText("Anas");
        textPlayerList.add( (TextView)findViewById(R.id.lPlayerName3TextView));
        textPlayerList.get(3).setText("Jakob");
        textPlayerList.add( (TextView)findViewById(R.id.lPlayerName4TextView));
        textPlayerList.get(4).setText("Johan");
        textPlayerList.add( (TextView)findViewById(R.id.lPlayerName5TextView));
        textPlayerList.get(5).setText("Blah");
        textPlayerList.add( (TextView)findViewById(R.id.lPlayerName6TextView));
        textPlayerList.get(6).setText("Bingo");
        textPlayerList.add( (TextView)findViewById(R.id.lPlayerName7TextView));
        textPlayerList.get(7).setText("Bajs");

    }


}
