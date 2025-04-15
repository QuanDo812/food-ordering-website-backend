package com.quan.service.impl;

import com.quan.model.Food;
import com.quan.model.Restaurant;
import com.quan.model.Review;
import com.quan.model.User;
import com.quan.repository.FoodRepository;
import com.quan.repository.RestaurantRepository;
import com.quan.repository.ReviewRepository;
import com.quan.request.CreateReviewRequest;
import com.quan.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    @Override
    public List<Review> getAllReviews(Long foodId) {
        return reviewRepository.findByFoodId(foodId);
    }

    @Override
    public Review createReview(CreateReviewRequest req, User user) {
        Food food = foodRepository.findById(req.getFoodId()).get();
        Restaurant restaurant = food.getRestaurant();

        Review review = new Review();
        review.setComment(req.getComment());
        review.setRating(req.getRating());
        review.setCustomer(user);
        review.setNameCustomer(user.getFullName());
        review.setReviewDate(LocalDateTime.now());
        review.setFood(food);
        Review savedReview =  reviewRepository.save(review);
        restaurant.setReviewCount(restaurant.getReviewCount()+1);
        restaurant.setRating(((restaurant.getRating()*(restaurant.getReviewCount()-1) + req.getRating())*1.0) / (1.0*restaurant.getReviewCount()));
        restaurantRepository.save(restaurant);
        return savedReview;
    }
}
