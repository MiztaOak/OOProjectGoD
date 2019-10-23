package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

public class CategoryVoteResultEvent implements IEvent {
    private String categoryId;

    public CategoryVoteResultEvent(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
