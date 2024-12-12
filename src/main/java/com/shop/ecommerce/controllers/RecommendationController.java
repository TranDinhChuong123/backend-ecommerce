package com.shop.ecommerce.controllers;

import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.models.UserProductInteraction;
import com.shop.ecommerce.repositories.UserProductInteractionRepository;
import com.shop.ecommerce.requests.ProductFilterRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recommendation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping ("/user")
    public ApiResponse<List<Product>> getRecommendationsForUser(@AuthenticationPrincipal UserDetails userDetails) {
        return new ApiResponse<>(
                200,
                "getRecommendationsForUser successfully",
                recommendationService.getRecommendationsForUser(userDetails.getUsername())
        );
    }
}
