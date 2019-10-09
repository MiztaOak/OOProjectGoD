package com.god.kahit.model;

public class VanityItem extends Item {

    public VanityItem(int price, String type, String name, String imageSource) {
        super(price, type, name, imageSource);
    }//TODO REMOVE THIS PLZ

    public VanityItem(int price, String type, String name) {
        super(price, type, name);
    }
}
