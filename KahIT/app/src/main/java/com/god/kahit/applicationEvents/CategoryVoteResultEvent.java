package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;
/**
 * This class is responsible for voting event.
 * This event is fired when players have voted for a category.
 *
 * used-by: This class is used in the following classes:
 * CategoryView, CategoryViewModel and Repository.
 *
 * @author: Mats Cedervall
 */

public class CategoryVoteResultEvent implements IEvent {
    private String categoryId;

    public CategoryVoteResultEvent(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
