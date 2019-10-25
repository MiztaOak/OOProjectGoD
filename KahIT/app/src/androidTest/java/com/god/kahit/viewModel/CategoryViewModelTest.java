package com.god.kahit.viewModel;

import com.god.kahit.model.Category;

import org.junit.Before;
import org.junit.Test;

public class CategoryViewModelTest {
    private CategoryViewModel categoryViewModel;

    @Before
    public void setup(){
        categoryViewModel = new CategoryViewModel();
    }

    @Test
    public void getCategoryIndex(){
        Category[] categories = categoryViewModel.getCategories();

    }
}
