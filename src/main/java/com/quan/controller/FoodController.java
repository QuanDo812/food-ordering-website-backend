package com.quan.controller;

import com.quan.model.Food;
import com.quan.model.Restaurant;
import com.quan.request.CreateFoodRequest;
import com.quan.response.MessageResponse;
import com.quan.service.FoodService;
import com.quan.service.RestaurantService;
import com.quan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService menuItemService;

    @Autowired
    private UserService userService;


    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(
            @RequestParam String name)  {
        List<Food> menuItem = menuItemService.searchFood(name);
        return ResponseEntity.ok(menuItem);
    }
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getMenuItemByRestaurantId(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String food_category) throws Exception {
        List<Food> menuItems= menuItemService.getRestaurantsFood(
                restaurantId,food_category);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) throws Exception {
        Food food = menuItemService.findFoodById(id);
        return ResponseEntity.ok(food);
    }

}
