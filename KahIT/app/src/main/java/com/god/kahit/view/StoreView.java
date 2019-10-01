package com.god.kahit.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.god.kahit.R;
import com.god.kahit.ViewModel.StoreViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

/**
 * StoreView a class for the view of the store where players can buy items
 * The items that are in the store will be the same for all players
 */
public class StoreView extends AppCompatActivity {
    private StoreViewModel storeViewModel = new StoreViewModel();
    private TextView pointsText, itemType1, itemType2, itemType3;
    private ArrayList<ImageView> itemsIcons = new ArrayList<>();  //Array list of imageButton
    private MutableLiveData<Integer> points;
    private Button itemButton1;
    private DrawerLayout drawerLayout;
    private ImageView storeImage;
    private List<Button> itemButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidenav_store);
        storeViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        initializeStoreView();
        itemButton1 = findViewById(R.id.itemButton);
        drawerLayout = findViewById(R.id.drawer_layout);
        storeImage = findViewById(R.id.storeImage);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                storeImage.setX(0);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                storeImage.setX(Resources.getSystem().getDisplayMetrics().widthPixels - 190);
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        });
        storeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.END);
            }
        });
    }

    public void initializeStoreView() {
        pointsText = findViewById(R.id.pointsText);
        storeViewModel.setPoints(500);
        findItemTypes();
        findItemIcons();
        findItemButtons();
        populateItemIcons();
        setPointsText();
        setItemTypes();
        addActionsToTheButtons();
    }

    public void populateItemIcons() {
        for (int i = 0; i < storeViewModel.getStoreItems().size(); i++) { // iterate through the list of items and get their names and their image source to set them correctly in the view
            int resId = getResources().getIdentifier(storeViewModel.getStoreItems().get(i).getImageSource(), "drawable", getPackageName()); //Gets the id of an item's image
            itemsIcons.get(i).setImageResource(resId); //Sets the image of the imageButton to the image of the item
        }
    }

    public void findItemTypes() {
        itemType1 = findViewById(R.id.itemType1);
        itemType2 = findViewById(R.id.itemType2);
        itemType3 = findViewById(R.id.itemType3);
    }

    public void findItemIcons() {

        itemsIcons.add((ImageView) findViewById(R.id.itemIcon)); // Add each imageButton in the view to the array list
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon2));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon3));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon4));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon5));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon6));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon7));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon8));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon9));

    }

    public void findItemButtons() {

        itemButtons.add((Button) findViewById(R.id.itemButton)); // Add each imageButton in the view to the array list
        itemButtons.add((Button) findViewById(R.id.itemButton2));
        itemButtons.add((Button) findViewById(R.id.itemButton3));
        itemButtons.add((Button) findViewById(R.id.itemButton4));
        itemButtons.add((Button) findViewById(R.id.itemButton5));
        itemButtons.add((Button) findViewById(R.id.itemButton6));
        itemButtons.add((Button) findViewById(R.id.itemButton7));
        itemButtons.add((Button) findViewById(R.id.itemButton8));
        itemButtons.add((Button) findViewById(R.id.itemButton9));

    }

    public void setPointsText() {
        pointsText.setText("Points:" + storeViewModel.getPoints().getValue());
    }

    public void setItemTypes() {
        itemType1.setText(storeViewModel.getStoreItems().get(0).getType());
        itemType2.setText(storeViewModel.getStoreItems().get(1).getType());
        itemType3.setText(storeViewModel.getStoreItems().get(2).getType());
    }

    public void addActionsToTheButtons() {

        for (final Button itemButton : itemButtons) {
            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemButtons.indexOf(itemButton) < 4) {
                        storeViewModel.buy(itemButtons.indexOf(itemButton));
                    }
                    itemButton.setEnabled(false);
                    itemsIcons.get(itemButtons.indexOf(itemButton)).setImageResource(R.drawable.checkmark);
                    pointsText.setText("Points: " + storeViewModel.getPoints().getValue());
                }
            });
        }
    }
}
