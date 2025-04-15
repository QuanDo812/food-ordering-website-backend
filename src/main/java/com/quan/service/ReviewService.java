package com.quan.service;

import com.quan.model.Review;
import com.quan.model.User;
import com.quan.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviews(Long foodId);

    Review createReview(CreateReviewRequest req, User user);

}
