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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/foods")
public class AdminFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Food food = foodService.createFood(req, req.getCategory(), restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        foodService.deleteFood(id);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("food deleted successfully!");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateAvailibilityStatus(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwt) throws Exception {
        Food food = foodService.updateAvailibilityStatus(id);
        return new ResponseEntity<>(food, HttpStatus.OK);

    }

}
