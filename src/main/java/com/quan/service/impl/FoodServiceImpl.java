package com.quan.service.impl;

import com.quan.model.Category;
import com.quan.model.Food;
import com.quan.model.Restaurant;
import com.quan.repository.FoodRepository;
import com.quan.request.CreateFoodRequest;
import com.quan.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) throws Exception {
        Food food=new Food();
        food.setFoodCategory(category);
        food.setCreationDate(new Date());
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setIngredientsItems(req.getIngredients());
        food.setRestaurant(restaurant);
        Food newFood = foodRepository.save(food);
        restaurant.getFoods().add(newFood);
        return newFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        foodRepository.delete(food);
    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId, String foodCategory) throws Exception {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);



        if(foodCategory!=null && !foodCategory.equals("")) {
            foods = filterByFoodCategory(foods, foodCategory);
        }

        return foods;
    }


    private List<Food> filterByFoodCategory(List<Food> foods, String foodCategory) {

        return foods.stream()
                .filter(food -> {
                    if (food.getFoodCategory() != null) {
                        return food.getFoodCategory().getName().equals(foodCategory);
                    }
                    return false; // Return true if food category is null
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Food food = foodRepository.findById(foodId).orElseThrow(
                ()-> new Exception("food not found"));
        return food;
    }

    @Override
    public Food updateAvailibilityStatus(Long foodId) throws Exception {
        Food food = findFoodById(foodId);

        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
