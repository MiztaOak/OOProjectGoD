package com.god.kahit.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.god.kahit.R;
import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.model.Item;
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
    private ArrayList<ImageView> boughtItemsIcons = new ArrayList<>();
    private List<Button> itemButtons = new ArrayList<>();
    static StoreView newInstance() {
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
    private void initializeStoreView() {
        findItemIcons();
        findItemButtons();
        findBoughtItemIcons();
        populateItemIcons();
        setPointsText();
        setButtonText();
        addActionsToButtons();
    }
    /**
     *A method that iterates through the list of items and get their names and their image source to set them correctly in the view
     */
    private void populateItemIcons() {
        Map<String,String> imageNameMap = ItemDataLoaderRealtime.getItemImageNameMap();
        for (int i = 0; i < itemButtons.size(); i++) {
            int resId = getResources().getIdentifier(imageNameMap.get(storeViewModel.getItemName(i)), "drawable", Objects.requireNonNull(getActivity()).getPackageName());
            itemsIcons.get(i).setImageResource(resId);
        }
    }

    /**
     * A method that finds all the imageViews and adds them to a list of imageViews
     */
    private void findItemIcons() {
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
     * A method that finds all the imageViews which will indicate the an item has been bought
     * and adds them to a list of imageViews
     */
    private void findBoughtItemIcons() {
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon10));
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon11));
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon12));
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon13));
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon14));
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon15));
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon16));
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon17));
        boughtItemsIcons.add((ImageView) Objects.requireNonNull(getView()).findViewById(R.id.itemIcon18));


    }
    /**
     * A method that finds all the buttons and adds them to a list of buttons
     */
    private void findItemButtons() {
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton2));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton3));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton4));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton5));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton6));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton7));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton8));
        itemButtons.add((Button) Objects.requireNonNull(getView()).findViewById(R.id.itemButton9));
    }
    /**
     * A method that updates the taxt the old the value of a player's points which changes after
     * buying an item or answering a question
     */
    @SuppressLint("SetTextI18n")
    private void setPointsText() {
        pointsText = Objects.requireNonNull(getView()).findViewById(R.id.pointsText);
        pointsText.setText("Points:" + storeViewModel.getPlayerPoints());
    }
    /**
     * A method that sets an action to each button that has been add to itemButtons list. The action
     * is a method call that lets the user buy an item
     */
    private void addActionsToButtons() {
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
    private void buy(Button itemButton){
        int i = itemButtons.indexOf(itemButton);
        if (storeViewModel.isItemBuyable(i)){
            storeViewModel.buy(i);
            Toast toast = Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                "You got " + storeViewModel.getItemName(i),
                Toast.LENGTH_LONG);
            toast.show();
            pointsText.setText("Points: " + storeViewModel.getPlayerPoints());
            disableButtons();
            //disableBoughtItems();
        }
    }
    /**
     * A method that disables the buttons used to buy items when the player's points are lower than
     * the price of an item
     */
    private void disableButtons(){
        for (int i = 0; i < itemButtons.size(); i++) {
            itemButtons.get(i).setEnabled(storeViewModel.isItemBuyable(i));
        }
    }
    /**
     * A method that sets the text of a button to the price of the item associated to it
     */
    @SuppressLint("SetTextI18n")
    private void  setButtonText(){
        for (int i = 0; i < itemButtons.size(); i++) {
            itemButtons.get(i).setText(Integer.toString(storeViewModel.getStoreItems().get(i).getPrice()));
        }
    }

    private void disableBoughtItems(){
        /*
        int i = storeViewModel.getStoreItems().indexOf(item);
        itemButtons.get(i).setEnabled(false);
        boughtItemsIcons.get(i).setVisibility(View.VISIBLE);
*/
    }
}
