package com.quan.service;


import com.quan.model.IngredientCategory;
import com.quan.model.IngredientsItem;

import java.util.List;

public interface IngredientsService {

    public IngredientCategory createIngredientsCategory(
            String name,Long restaurantId) throws Exception;

    public IngredientCategory findIngredientsCategoryById(Long id) throws Exception;

    public List<IngredientCategory> findIngredientsCategoryByRestaurantId(Long id) throws Exception;

    public List<IngredientsItem> findRestaurantsIngredients(
            Long restaurantId);


    public IngredientsItem createIngredientsItem(Long restaurantId,
                                                 String ingredientName,Long ingredientCategoryId, Long price) throws Exception;

    public IngredientsItem updateStoke(Long id) throws Exception;

}
