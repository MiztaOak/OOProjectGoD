package com.god.kahit.Events;

public class CategoryVoteResultEvent {
    private String categoryId;

    public CategoryVoteResultEvent(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
