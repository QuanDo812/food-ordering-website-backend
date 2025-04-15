package com.quan.service;

import com.quan.dto.RestaurantDTO;
import com.quan.dto.RestaurantUser;
import com.quan.model.Restaurant;
import com.quan.model.User;
import com.quan.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest req, User user);

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest req);

    public void deleteRestaurant(Long restaurantId) throws Exception;

    public List<Restaurant> getAllRestaurants();

    public List<Restaurant> searchRestaurant(String keyword);

    public Restaurant findRestaurantById(Long restaurantId) throws Exception;

    public Restaurant findAllRestaurantByUserId(Long userId) throws Exception;

    public RestaurantUser addToFavorite(Long restaurantId, Long userId) throws Exception;

    public Restaurant updateRestaurantStatus(Long restaurantId) throws Exception;

}
