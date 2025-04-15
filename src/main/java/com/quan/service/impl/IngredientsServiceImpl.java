package com.quan.service.impl;

import com.quan.model.IngredientCategory;
import com.quan.model.IngredientsItem;
import com.quan.model.Restaurant;
import com.quan.repository.IngredientCategoryRepository;
import com.quan.repository.IngredientsItemRepository;
import com.quan.repository.RestaurantRepository;
import com.quan.service.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientsServiceImpl implements IngredientsService {

    @Autowired
    private IngredientsItemRepository ingredientsItemRepository;

    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public IngredientCategory createIngredientsCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setName(name);
        ingredientCategory.setRestaurant(restaurant);
        return ingredientCategoryRepository.save(ingredientCategory);
    }

    @Override
    public IngredientCategory findIngredientsCategoryById(Long id) throws Exception {
        return ingredientCategoryRepository.findById(id).orElseThrow(
                () -> new Exception("not found ingredients category!")
        );
    }

    @Override
    public List<IngredientCategory> findIngredientsCategoryByRestaurantId(Long id) throws Exception {
        return ingredientCategoryRepository.findByRestaurantId(id);
    }

    @Override
    public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {
        return ingredientsItemRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem createIngredientsItem(Long restaurantId, String ingredientName, Long ingredientCategoryId, Long price) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId).get();
        IngredientsItem ingredientsItem = new IngredientsItem();
        ingredientsItem.setRestaurant(restaurant);
        ingredientsItem.setIngredientCategory(ingredientCategory);
        ingredientsItem.setName(ingredientName);
        ingredientsItem.setPrice(price);
        return ingredientsItemRepository.save(ingredientsItem);
    }

    @Override
    public IngredientsItem updateStoke(Long id) throws Exception {
        IngredientsItem ingredientsItem = ingredientsItemRepository.findById(id).orElseThrow(
                () -> new Exception("not found ingredients item!")
        );
        ingredientsItem.setInStoke(!ingredientsItem.isInStoke());
        return ingredientsItemRepository.save(ingredientsItem);
    }
}
