package com.god.kahit.viewModel;


import android.util.Log;

import com.god.kahit.Events.CategoryVoteResultEvent;
import com.god.kahit.Events.NewViewEvent;
import com.god.kahit.Events.PlayerVotedCategoryEvent;
import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Category;
import com.god.kahit.model.Player;
import com.god.kahit.view.QuestionView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

import static com.god.kahit.model.QuizGame.BUS;

public class CategoryViewModel extends ViewModel implements LifecycleObserver {
    private static final String LOG_TAG = CategoryViewModel.class.getSimpleName();
    private Repository repository;
    private Category[] categories;
    private int[] votes;
    private int totNmbVotes;
    private int curNmbVotes;

    public CategoryViewModel() {
        repository = Repository.getInstance();
    }

    public void generateCategories() {
        repository.generateRandomCategoryArray(4);
        updateCategories();
    }

    public void updateCategories() {
        categories = repository.getCategorySelectionArray();
    }

    public boolean isMe(Player player) {
        return repository.isMe(player.getId());
    }

    public boolean isHotSwap() {
        return repository.isHotSwap();
    }

    public boolean isHost() {
        return repository.isHost();
    }

    public void sendIsReady() {
        Log.d(LOG_TAG, "sendIsReady: called. Now waiting for server..");
        repository.setMyReadyStatus(true);
    }

    public String getMyPlayerId() {
        if (isHost()) {
            return repository.getHostPlayerId();
        } else {
            return repository.getClientPlayerId();
        }
    }

    public void resetPlayersReady() {
        repository.resetPlayersReady();
    }

    public void showNextView() {
        Class<?> newViewClass = QuestionView.class; //todo get actual next view, it's not always QuestionView
        repository.broadcastShowNewView(newViewClass);
        BUS.post(new NewViewEvent(newViewClass));
    }

    public void resetVote() {
        votes = new int[categories.length];
        totNmbVotes = repository.getPlayers().size();
        curNmbVotes = 0;
    }

    public int getCategoryIndex(String categoryId) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].getId().equals(categoryId)) {
                return i;
            }
        }

        Log.d(LOG_TAG, String.format("getCategoryIndex: unable to find category in votable " +
                        "categories: '%s'. Sought after categoryId: '%s' , returning -1.",
                Arrays.toString(categories), categoryId));
        return -1;
    }

    public void onCategoryVoteEvent(String votedCategoryId) {
        Log.d(LOG_TAG, "onCategoryVoteEvent: called.");
        int votedIndex = getCategoryIndex(votedCategoryId);

        curNmbVotes++;
        votes[votedIndex]++;
        if (curNmbVotes >= totNmbVotes) {
            int resultIndex = getVoteResult();
            repository.broadcastCategoryVoteResult(categories[resultIndex]);
            BUS.post(new CategoryVoteResultEvent(categories[resultIndex].getId()));
        }
    }

    public void voteCategory(int index) {
        if (isHost()) {
            BUS.post(new PlayerVotedCategoryEvent(repository.getHostPlayer(), categories[index].getId()));
        } else {
            repository.sendCategoryVote(categories[index].getId());
        }
    }

    private int getVoteResult() {
        int mostNmbVotes = votes[0];
        List<Integer> drawCategoryIndexes = new ArrayList<>();

        //Populate drawCategoryIndexes with the indexes of all categories with same number of votes
        for (int i = 0; i < votes.length; i++) {
            int curIndexVotes = votes[i];

            if (curIndexVotes > mostNmbVotes) {
                mostNmbVotes = curIndexVotes;
                drawCategoryIndexes.clear();
                drawCategoryIndexes.add(i);

            } else if (curIndexVotes == mostNmbVotes) {
                drawCategoryIndexes.add(i);
            }
        }

        //Return one index of all the categories at random
        Random random = new Random();
        int randomCategoryIndex = random.nextInt(drawCategoryIndexes.size());
        return drawCategoryIndexes.get(randomCategoryIndex);
    }

    public void setCategory(int index) {
        repository.setCurrentCategory(categories[index]);
    }

    public Category[] getCategories() {
        return categories;
    }
}