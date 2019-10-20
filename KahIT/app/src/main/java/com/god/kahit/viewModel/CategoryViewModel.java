package com.god.kahit.viewModel;


import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

import com.god.kahit.Repository.Repository;
import com.god.kahit.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryViewModel extends ViewModel implements LifecycleObserver {
    private List<Category> categories;

    public CategoryViewModel() {
        generateCategories();
    }

    private void generateCategories() {
        Category currentCategory = Repository.getInstance().getCurrentCategory();
        categories = new ArrayList<>(Category.getRealCategories());
        categories.remove(currentCategory);
        Collections.shuffle(categories);
        categories = categories.subList(0, 4);
    }

    public void setCategory(int index) {
        Repository.getInstance().setCurrentCategory(categories.get(index));
    }

    public List<Category> getCategories() {
        return categories;
    }
}