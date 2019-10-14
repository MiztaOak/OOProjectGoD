package com.god.kahit.view;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * StoreView a class for the view of the store where players can buy items
 * The items that are in the store will be the same for all players
 */
public class StoreView extends Fragment {
    private StoreViewModel storeViewModel = new StoreViewModel();
    private TextView pointsText;
    private ArrayList<ImageView> itemsIcons = new ArrayList<>();
    private List<Button> itemButtons = new ArrayList<>();
    public static StoreView newInstance() {
        return new StoreView();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sidenav_store, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        storeViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        initializeStoreView();
        // TODO: Use the ViewModel
    }
    /**
     * A method that calls all other methods that the view requires.
     * This method gets called upon creating the view
     */
    public void initializeStoreView() {
        findItemIcons();
        findItemButtons();
        populateItemIcons();
        setPointsText();
        setButtonText();
        addActionsToButtons();
    }
    /**
     *A method that iterates through the list of items and get their names and their image source to set them correctly in the view
     */
    public void populateItemIcons() {
        Map<String,String> imageNameMap = ItemDataLoaderRealtime.getItemImageNameMap();
        for (int i = 0; i < storeViewModel.getStoreItems().size(); i++) {
            int resId = getResources().getIdentifier(imageNameMap.get(storeViewModel.getStoreItems().get(i).getName()), "drawable", getActivity().getPackageName());
            itemsIcons.get(i).setImageResource(resId);
        }
    }

    /**
     * A method that finds all the imageViews and adds them to a list of imageViews
     */
    public void findItemIcons() {
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon));
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon2));
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon3));
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon4));
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon5));
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon6));
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon7));
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon8));
        itemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon9));

    }
    /**
     * A method that finds all the buttons and adds them to a list of buttons
     */
    public void findItemButtons() {
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton2));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton3));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton4));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton5));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton6));
        /*
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton7));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton8));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton9));
         */
    }
    /**
     * A method that updates the taxt the old the value of a player's points which changes after
     * buying an item or answering a question
     */
    @SuppressLint("SetTextI18n")
    public void setPointsText() {
        pointsText = Objects.requireNonNull(getView()).findViewById(R.id.pointsText);
        pointsText.setText("Points:" + storeViewModel.getStoreModel().getPlayer().getScore());
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
    @SuppressLint("SetTextI18n")
    public void buy(Button itemButton){
        if (storeViewModel.isItemBuyable(storeViewModel.getStoreItems().get(itemButtons.indexOf(itemButton)))){
            storeViewModel.buy(itemButtons.indexOf(itemButton));
            Toast toast = Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                "You got " + storeViewModel.getStoreItems().get(itemButtons.indexOf(itemButton)).getName(),
                Toast.LENGTH_LONG);
            toast.show();
            itemButton.setEnabled(false);
            itemsIcons.get(itemButtons.indexOf(itemButton)).setImageResource(R.drawable.checkmark);
            pointsText.setText("Points: " + storeViewModel.getStoreModel().getPlayer().getScore());
            disableButtons();
        }
    }
    /**
     * A method that disables the buttons used to buy items when the player's points are lower than
     * the price of an item
     */
    public void disableButtons(){

        for (int i = 0; i < itemButtons.size(); i++) {
            if(!storeViewModel.isItemBuyable(storeViewModel.getStoreItems().get(i))){
                itemButtons.get(i).setEnabled(false);
            }
        }
    }
    /**
     * A method that sets the text of a button to the price of the item associated to it
     */
    @SuppressLint("SetTextI18n")
    public void setButtonText(){
        for (int i = 0; i < itemButtons.size(); i++) {
            itemButtons.get(i).setText(Integer.toString(storeViewModel.getStoreItems().get(i).getPrice()));
        }
    }
}
