package com.quan.repository;

import com.quan.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Restaurant findByOwnerId(Long id);

    @Query("select r from Restaurant r where lower(r.name) like lower(concat('%', ?1, '%'))" +
            " or lower(r.cuisineType) like lower(concat('%', ?1, '%'))")
    List<Restaurant> searchRestaurant(String keyword);

}
