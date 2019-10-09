package com.god.kahit.model;

public abstract class Item {
    private int price;
    private String type; //TODO remove
    private String name;
    private String imageSource; //TODO check if this is ok. -It is not. Use name as part of a sourcepath perhaps instead //Mats

    public Item(int price, String type, String name) {
        this.price = price;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
