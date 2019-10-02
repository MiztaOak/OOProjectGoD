package com.god.kahit.ViewModel;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModel;

import com.god.kahit.Repository;
import com.god.kahit.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryViewModel extends ViewModel implements LifecycleObserver{
    private List<Category> categories = new ArrayList<>();

    public CategoryViewModel(){
        generateCategories();
    }

    private void generateCategories(){
        Category currentCategory = Repository.getInstance().getCurrentCategory();
        Random r = new Random();
        for(int i = 0; i < 4; i++){
            Category category;
            do{
                category = Category.getCategoryByIndex(r.nextInt(Category.values().length)-1);
            }while(category == currentCategory || listContainsCategory(categories,category));
            categories.add(category);
        }
    }

    private boolean listContainsCategory(List<Category> list, Category category){
        for(Category listItem: list){
            if(listItem.toString().equals(category.toString())){
                return true;
            }
        }
        return false;
    }

    public void setCategory (int index){
        Repository.getInstance().setCurrentCategory(categories.get(index));
    }

    public List<Category> getCategories() {
        return categories;
    }
}