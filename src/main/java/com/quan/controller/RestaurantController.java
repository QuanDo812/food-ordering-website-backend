package com.quan.controller;

import com.quan.dto.RestaurantDTO;
import com.quan.dto.RestaurantUser;
import com.quan.model.Restaurant;
import com.quan.model.User;
import com.quan.request.CreateRestaurantRequest;
import com.quan.service.RestaurantService;
import com.quan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurant(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwt(jwt);
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> findRestaurant(@RequestHeader("Authorization") String jwt,
                                                     @RequestParam String keyword) {
        User user = userService.findUserByJwt(jwt);
        List<Restaurant> restaurants = restaurantService.searchRestaurant(keyword);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PutMapping("/{id}/add-favorites")
    public ResponseEntity<RestaurantUser> addToFavorites(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long id) throws Exception {
        User user = userService.findUserByJwt(jwt);
        RestaurantUser restaurantDTO = restaurantService.addToFavorite(id, user.getId());
        return new ResponseEntity<>(restaurantDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(@RequestHeader("Authorization") String jwt,
                                                        @PathVariable Long id) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}
