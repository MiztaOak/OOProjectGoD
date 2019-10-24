package com.god.kahit.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CategoryTest {
    @Test
    public void getCategoryByStringOrIdTest(){
        List<Category> categories = new ArrayList<>(Category.getRealCategories());
        categories.add(Category.Test);
        categories.add(Category.Mix);
        for (Category category: categories){
            Assert.assertEquals(Category.getCategoryById(category.getId()),category);
            Assert.assertEquals(Category.getCategoryByString(category.toString()),category);
        }
        Assert.assertNull(Category.getCategoryByString(""));
        Assert.assertNull(Category.getCategoryById(""));
    }
}
