package com.god.kahit.model;

import android.widget.ImageButton;

import java.util.List;

public class Player {
    private String name;
    private ImageButton selfie;
    private int score;
    private List<Item> items;
    private Modifier currentEffcts;


    public Player(String name, int score, List<Item> items) {
        this.name = name;
        this.score = score;
        this.items = items;
    }

    public void calculateNewScore( int newScore){
        // todo switch instead
        if (currentEffcts.equals("double score")){
            updateScore(newScore);
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void updateScore(int points) {
       this.score += score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public List<Item> getListItems() {
        return items;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void addItemByIndex(int index, Item item){
        items.add(index,item);
    }

    public void removeItemByName(Item item){
        items.remove(item);
    }

    public void removeItemByIndex(int item){
        items.remove(item);
    }
}
