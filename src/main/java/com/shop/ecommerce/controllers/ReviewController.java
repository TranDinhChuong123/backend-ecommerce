package com.shop.ecommerce.controllers;

import com.shop.ecommerce.requests.CancelOrderRequest;
import com.shop.ecommerce.requests.ReviewRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.responses.ReviewResponse;
import com.shop.ecommerce.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/create")
    public ApiResponse<Boolean> createNewReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ReviewRequest request
    ) {
        request.setUserId(userDetails.getUsername());
        return new ApiResponse<>(
                200,
                "Create New Review successfully",
                reviewService.createNewReview(request)
        );
    }

    @GetMapping("/productId/{productId}")
    public ApiResponse<List<ReviewResponse>> getReviewsByProductId(@PathVariable String productId) {
        return new ApiResponse<>(
                200,
                "GET Reviews Buy ProductId successfully",
                reviewService.getReviewsByProductId(productId)
        );
    }

    @GetMapping("/userId")
    public ApiResponse<List<ReviewResponse>> getReviewsByUserId(@AuthenticationPrincipal UserDetails userDetails) {
        return new ApiResponse<>(
                200,
                "GET Reviews By UserId successfully",
                reviewService.getReviewsByUserId(userDetails.getUsername())
        );
    }


}
