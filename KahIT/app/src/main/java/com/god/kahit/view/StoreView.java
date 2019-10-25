package com.god.kahit.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.god.kahit.R;
import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.viewModel.StoreViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * responsibility: a fragment class for the view of the store where players can buyItem items.
 * This fragment class is included in the QuestionView class.
 * used-by: This class is used in the following classes:
 * QuestionView.
 *
 * @author Anas Alkoutli
 */
class StoreView extends Fragment {
    private StoreViewModel storeViewModel = new StoreViewModel();
    private final ArrayList<ImageView> itemsIcons = new ArrayList<>();
    private final List<Button> itemButtons = new ArrayList<>();
    private final ArrayList<ImageView> boughtItemsIcons = new ArrayList<>();

    /**
     * A method that returns a creates a new StoreView object and returns it.
     *
     * @return : A StoreView that the method created.
     */
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
        disableButtons();
        disableBoughtItems();
    }

    /**
     * A method that iterates through the list of items and get their names and their image source to set them correctly in the view.
     */
    private void populateItemIcons() {
        Map<String, String> imageNameMap = ItemDataLoaderRealtime.getItemImageNameMap();
        for (int i = 0; i < itemButtons.size(); i++) {
            int resId = getResources().getIdentifier(imageNameMap.get(storeViewModel.getItem(i).getName()), "drawable", getActivity().getPackageName());
            itemsIcons.get(i).setImageResource(resId);
        }
    }

    /**
     * A method that finds all the imageViews and adds them to a list of imageViews.
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
     * A method that finds all the buttons and adds them to a list of buttons.
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
     * A method that finds all the ImageViews to add an image (sold)
     * when they item is bought later.
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
     * A method that updates the taxt the old the value of a player's points which changes after
     * buying an item or answering a question
     */
    @SuppressLint("SetTextI18n")
    private void setPointsText() {
        TextView pointsText = Objects.requireNonNull(getView()).findViewById(R.id.pointsText);
        pointsText.setText("Points:" + storeViewModel.getPlayerPoints());
    }

    /**
     * A method that sets an action to each button that has been add to itemButtons list. The action
     * is a method call that lets the user buyItem an item
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
     * A method that lets the user buyItem an item from store and displays that the item has been
     * bought by a graphical confirmation and a toast message
     *
     * @param itemButton is which button has been clicked which is need in order to buyItem the
     *                   associated item
     */
    @SuppressLint("SetTextI18n")
    private void buy(Button itemButton) {
        int i = itemButtons.indexOf(itemButton);
        if (storeViewModel.isItemBuyable(i)) {
            storeViewModel.buy(i);
            showToast(i);
            setPointsText();
            disableButtons();
            setDisableEffect(i);
        }
    }

    private void showToast(int i) {
        Toast toast = Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                "You got " + storeViewModel.getItem(i).getName(),
                Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * A method that disables the buttons used to buyItem items when the player's points are lower than
     * the price of an item
     */
    private void disableButtons() {
        for (int i = 0; i < itemButtons.size(); i++) {
            if (!storeViewModel.isItemBuyable(i)) {
                itemButtons.get(i).setEnabled(false);
            }
        }
    }

    /**
     * A method that sets the text of a button to the price of the item associated to it
     */
    @SuppressLint("SetTextI18n")
    private void setButtonText() {
        for (int i = 0; i < itemButtons.size(); i++) {
            itemButtons.get(i).setText(Integer.toString(storeViewModel.getItem(i).getPrice()));
        }
    }

    /**
     * A method that checks which items are bought to be disabled.
     */
    private void disableBoughtItems() {
        for (int i = 0; i < itemButtons.size(); i++) {
            if (storeViewModel.isItemBought(i)) {
                setDisableEffect(i);
            }
        }
    }

    /**
     * A method that disables a bought item so only one player can buyItem the item
     * until the store gets restocked.
     *
     * @param i: The index of the item to be disabled.
     */
    private void setDisableEffect(int i) {
        itemButtons.get(i).setEnabled(false);
        boughtItemsIcons.get(i).setVisibility(View.VISIBLE);
    }
}
