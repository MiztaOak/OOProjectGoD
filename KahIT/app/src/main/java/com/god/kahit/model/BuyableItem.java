package com.god.kahit.model;

import java.security.PrivateKey;

public class BuyableItem {
    private int price;
    private String type;
    private User user;
    private String name;
    private String imageSource;

    public BuyableItem(int price, String type, String name, String imageSource){
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
