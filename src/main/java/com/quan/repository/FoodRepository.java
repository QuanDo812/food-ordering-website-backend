package com.quan.repository;

import com.quan.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByRestaurantId(Long id);

    @Query("select f from Food f where lower(f.name) like lower(concat('%', ?1, '%'))" +
            " or lower(f.foodCategory) like lower(concat('%', ?1, '%'))")
    List<Food> searchFood(String keyword);

}
