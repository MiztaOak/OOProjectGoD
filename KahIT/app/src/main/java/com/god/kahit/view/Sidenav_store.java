package com.god.kahit.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Store;

import java.util.ArrayList;

public class Sidenav_store extends AppCompatActivity {

    private Store storeModel= new Store();
    private TextView  pointsText, itemType1, itemType2, itemType3;
    private ArrayList<ImageButton> itemsIcons = new ArrayList<ImageButton>();  //Array list of imageButton
    private int points = 500;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidenav_store);
        pointsText = findViewById(R.id.pointsText);
        itemType1 = findViewById(R.id.itemType1);
        itemType2 = findViewById(R.id.itemType2);
        itemType3 = findViewById(R.id.itemType3);
        itemsIcons.add((ImageButton) findViewById(R.id.itemIcon)); // Add each imageButton in the view to the array list
        itemsIcons.add((ImageButton) findViewById(R.id.itemIcon2));
        itemsIcons.add((ImageButton) findViewById(R.id.itemIcon3));
        itemsIcons.add((ImageButton) findViewById(R.id.itemIcon4));

        String text = ""; // for adding the name of each statAlterer to a string and display them in textView

        for (int i = 0; i < storeModel.getStoreItems().size(); i++) { // iterate through the list of items and get their names and their image source to set them correctly in the view
            text += (storeModel.getStoreItems().get(i).getName()+ "\n");
            int resId = getResources().getIdentifier(storeModel.getStoreItems().get(i).getImageSource() , "drawable", getPackageName()); //Gets the id of an item's image
            itemsIcons.get(i).setImageResource(resId); //Sets the image of the imageButton to the image of the item
        }
        //storeText.setText(text);
        pointsText.setText("Points:" + points);
        itemType1.setText(storeModel.getStoreItems().get(0).getType());
        for ( final ImageButton itemButton: itemsIcons){
            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buy(itemsIcons.indexOf(itemButton));

                }
            });
        }
    }
    public void buy(int i){
        points = points-(storeModel.getStoreItems().get(i).getPrice());
        pointsText.setText("Points: " + points);
    }
}
