package com.god.kahit.model;

public abstract class Item {
    private int price;
    private String type; //TODO remove
    private String name;
    private String imageSource; //TODO REMOVE THIS PLZ

    public Item(int price, String type, String name, String imageSource) {//TODO REMOVE THIS PLZ
        this.price = price;
        this.type = type;
        this.name = name;
        this.imageSource = imageSource;
    }

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

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
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
