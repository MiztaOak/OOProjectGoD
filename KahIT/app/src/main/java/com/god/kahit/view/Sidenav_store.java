package com.god.kahit.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.model.Store;

public class Sidenav_store extends AppCompatActivity {
    private Store storeModel= new Store();
    private TextView storeText;
    private ImageButton[] itemIcons = new ImageButton[4]; //Array of imageButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidenav_store);
        storeText = findViewById(R.id.storeText);

        itemIcons[0]= findViewById(R.id.itemIcon); // Add each imageButton in the view to the array
        itemIcons[1]= findViewById(R.id.itemIcon2);
        itemIcons[2]= findViewById(R.id.itemIcon3);
        itemIcons[3]= findViewById(R.id.itemIcon4);
        String text = ""; // for adding the name of each statAlterer to a string and display them in textView

        for (int i = 0; i < storeModel.getStoreItems().size(); i++) { // iterate through the list of items and get their names and their image source to set them correctly in the view
            text += (storeModel.getStoreItems().get(i).getName()+ "\n");
            int resId = getResources().getIdentifier(storeModel.getStoreItems().get(i).getImageSource() , "drawable", getPackageName()); //Gets the id of an item's image
            itemIcons[i].setImageResource(resId); //Sets the image of the imageButton to the image of the item
        }
        storeText.setText(text);
        storeText.setText(storeModel.getStoreItems().get(0).getName());
    }
}
