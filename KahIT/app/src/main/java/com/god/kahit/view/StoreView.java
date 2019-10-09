package com.god.kahit.view;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.god.kahit.R;
import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.viewModel.StoreViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.core.view.GravityCompat;

/**
 * StoreView a class for the view of the store where players can buy items
 * The items that are in the store will be the same for all players
 */
public class StoreView extends AppCompatActivity {
    private StoreViewModel storeViewModel = new StoreViewModel();
    private TextView pointsText, itemType1, itemType2, itemType3;
    private ArrayList<ImageView> itemsIcons = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ImageView storeImage;
    private List<Button> itemButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidenav_store);

        initializeStoreView();
    }

    /**
     * A method that calls all other methods that the view requires.
     * This method gets called upon creating the view
     */
    public void initializeStoreView() {
        findViews();
        findItemIcons();
        findItemButtons();
        populateItemIcons();
        setPointsText();
        setItemTypes();
        setButtonText();
        addActionsToButtons();
        addDrawerListener();
    }
    /**
     *A method that iterates through the list of items and get their names and their image source to set them correctly in the view
     */
    public void populateItemIcons() {
        Map<String,String> imageNameMap = ItemDataLoaderRealtime.getItemImageNameMap();
        for (int i = 0; i < storeViewModel.getStoreItems().size(); i++) {
            int resId = getResources().getIdentifier(imageNameMap.get(storeViewModel.getStoreItems().get(i).getName()), "drawable", getPackageName());
            itemsIcons.get(i).setImageResource(resId);
        }
    }
    /**
     * A method that finds all the necessary views by their id in order for this view to work
     */
    public void findViews(){
        drawerLayout = findViewById(R.id.drawer_layout);
        storeImage = findViewById(R.id.storeImage);
        pointsText = findViewById(R.id.pointsText);
        itemType1 = findViewById(R.id.itemType1);
        itemType2 = findViewById(R.id.itemType2);
        itemType3 = findViewById(R.id.itemType3);
    }
    /**
     * A method that finds all the imageViews and adds them to a list of imageViews
     */
    public void findItemIcons() {
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon2));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon3));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon4));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon5));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon6));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon7));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon8));
        itemsIcons.add((ImageView) findViewById(R.id.itemIcon9));

    }
    /**
     * A method that finds all the buttons and adds them to a list of buttons
     */
    public void findItemButtons() {

        itemButtons.add((Button) findViewById(R.id.itemButton)); // Add each imageButton in the view to the array list
        itemButtons.add((Button) findViewById(R.id.itemButton2));
        itemButtons.add((Button) findViewById(R.id.itemButton3));
        itemButtons.add((Button) findViewById(R.id.itemButton4));
        itemButtons.add((Button) findViewById(R.id.itemButton5));
        itemButtons.add((Button) findViewById(R.id.itemButton6));
        //itemButtons.add((Button) findViewById(R.id.itemButton7)); //Empty buttons for now
        //itemButtons.add((Button) findViewById(R.id.itemButton8)); //Empty buttons for now
        //itemButtons.add((Button) findViewById(R.id.itemButton9)); //Empty buttons for now
    }
    /**
     * A method that adds an action to the drawer layout which changes the position of storeImage
     * upon openning and closing
     */
    public void addDrawerListener(){
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
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }
    /**
     * A method that updates the taxt the old the value of a player's points which changes after
     * buying an item or answering a question
     */
    public void setPointsText() {
        pointsText.setText("Points:" + storeViewModel.getStoreModel().getPlayer().getScore());
    }
    /**
     * A method that sets the type of a group of items in their textviews
     */
    public void setItemTypes() {
        itemType1.setText(storeViewModel.getStoreItems().get(0).getType());
        itemType2.setText(storeViewModel.getStoreItems().get(1).getType());
        itemType3.setText(storeViewModel.getStoreItems().get(2).getType());
    }
    /**
     * A method that sets an action to each button that has been add to itemButtons list. The action
     * is a method call that lets the user buy an item
     */
    public void addActionsToButtons() {
        for (final Button itemButton : itemButtons) {
            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buy(itemButton);
                }
            });
        }
    }
    /**
     * A method that lets the user buy an item from store and displays that the item has been
     * bought by a graphical confirmation and a toast message
     *
     * @param itemButton is which button has been clicked which is need in order to buy the
     * associated item
     */
    public void buy(Button itemButton){
        if (storeViewModel.isItemBuyable(storeViewModel.getStoreItems().get(itemButtons.indexOf(itemButton)))){
            storeViewModel.buy(itemButtons.indexOf(itemButton));
            Toast toast = Toast.makeText(getApplicationContext(),
                "You got a " + storeViewModel.getStoreItems().get(itemButtons.indexOf(itemButton)).getType() + " " +storeViewModel.getStoreItems().get(itemButtons.indexOf(itemButton)).getName(),
                Toast.LENGTH_LONG);
            toast.show();
            itemButton.setEnabled(false);
            itemsIcons.get(itemButtons.indexOf(itemButton)).setImageResource(R.drawable.checkmark);
            pointsText.setText("Points: " + storeViewModel.getStoreModel().getPlayer().getScore());
            disablebuttons();
        }
    }
    /**
     * A method that disables the buttons used to buy items when the player's points are lower than
     * the price of an item
     */
    public void disablebuttons(){
        for (int i = 0; i < itemButtons.size(); i++) {
            if(!storeViewModel.isItemBuyable(storeViewModel.getStoreItems().get(i))){
                itemButtons.get(i).setEnabled(false);
            }
        }
    }
    /**
     * A method that sets the text of a button to the price of the item associated to it
     */
    public void setButtonText(){
        for (int i = 0; i < itemButtons.size(); i++) {
            itemButtons.get(i).setText(Integer.toString(storeViewModel.getStoreItems().get(i).getPrice()));
        }
    }
}
