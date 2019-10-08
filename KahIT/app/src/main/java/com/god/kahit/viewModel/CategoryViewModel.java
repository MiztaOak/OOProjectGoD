package com.god.kahit.viewModel;


import com.god.kahit.Repository;
import com.god.kahit.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

public class CategoryViewModel extends ViewModel implements LifecycleObserver {
    private List<Category> categories = new ArrayList<>();

    public CategoryViewModel() {
        generateCategories();
    }

    private void generateCategories() {
        Category currentCategory = Repository.getInstance().getCurrentCategory();
        categories = Category.getRealCategories();
        categories.remove(currentCategory);
        Collections.shuffle(categories);
        categories = categories.subList(0,4);
    }

    private boolean listContainsCategory(List<Category> list, Category category) {
        for (Category listItem : list) {
            if (listItem.toString().equals(category.toString())) {
                return true;
            }
        }
        return false;
    }

    public void setCategory(int index) {
        Repository.getInstance().setCurrentCategory(categories.get(index));
    }

    public List<Category> getCategories() {
        return categories;
    }
}