package com.god.kahit.model;

import java.util.List;

public class User {
    private String name;
    private int score;
    private List<Buyable> items;

    public User(String name, int score, List<Buyable> items) {
        this.name = name;
        this.score = score;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<Buyable> getItems() {
        return items;
    }
}
