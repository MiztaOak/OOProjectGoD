package com.god.kahit.model;

public class Item {
    private int price;
    private String type; //TODO remove
    private String name;
    private String imageSource; //TODO check if this is ok

    public Item(int price, String type, String name, String imageSource) {
        this.price = price;
        this.type = type;
        this.name = name;
        this.imageSource = imageSource;
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
