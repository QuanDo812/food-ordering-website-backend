package com.quan.service;

import com.quan.model.Category;

import java.util.List;

public interface CategoryService {

    public Category createCategory (String name,Long userId) throws Exception;
    public List<Category> findCategoryByRestaurantId(Long restaurantId) throws Exception;
    public Category findCategoryById(Long id) throws Exception;

}
