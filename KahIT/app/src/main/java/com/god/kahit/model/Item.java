package com.god.kahit.model;

public abstract class Item {
    private int price;
    private String name;

    public Item(int price, String name) {
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

}
