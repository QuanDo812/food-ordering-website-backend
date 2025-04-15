package com.quan.controller;

import com.quan.model.Food;
import com.quan.model.Review;
import com.quan.model.User;
import com.quan.request.CreateReviewRequest;
import com.quan.service.ReviewService;
import com.quan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping("/{foodId}")
    public ResponseEntity<List<Review>> getAllReviewsByFoodId(@PathVariable Long foodId){
        List<Review> reviews = reviewService.getAllReviews(foodId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Food> createReview(@RequestBody CreateReviewRequest req,
                                             @RequestHeader("Authorization")String jwt){
        User user = userService.findUserByJwt(jwt);
        Review review = reviewService.createReview(req, user);
        Food food = review.getFood();
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

}
