package com.shop.ecommerce.services;
import com.shop.ecommerce.models.Review;
import com.shop.ecommerce.requests.ReviewRequest;
import com.shop.ecommerce.responses.ReviewResponse;

import java.util.List;

public interface ReviewService {

    boolean createNewReview(ReviewRequest request);
    List<ReviewResponse> getReviewsByProductId(String productId);

    List<ReviewResponse> getReviewsByUserId(String userId);
}
