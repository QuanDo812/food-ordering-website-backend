package com.quan.service.impl;

import com.quan.model.Category;
import com.quan.model.Restaurant;
import com.quan.repository.CategoryRepository;
import com.quan.repository.RestaurantRepository;
import com.quan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Category createCategory(String name, Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);

        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);
        restaurant.getCategories().add(category);
        restaurantRepository.save(restaurant);
        return category;

    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long restaurantId){
        return categoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        return categoryRepository.findById(id).orElseThrow(
                () -> new Exception("not found category")
        );
    }
}
