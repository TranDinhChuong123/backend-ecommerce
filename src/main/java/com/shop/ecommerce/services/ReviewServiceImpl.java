package com.shop.ecommerce.services;

import com.shop.ecommerce.exception.NotFoundException;
import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.models.ProductVariation;
import com.shop.ecommerce.models.Review;
import com.shop.ecommerce.models.User;
import com.shop.ecommerce.repositories.ProductRepository;
import com.shop.ecommerce.repositories.ReviewRepository;
import com.shop.ecommerce.repositories.UserRepository;
import com.shop.ecommerce.requests.ReviewRequest;
import com.shop.ecommerce.responses.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);


    @Override
    public boolean createNewReview(ReviewRequest request) {

        Review review = Review.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .selectedVariationId(request.getSelectedVariationId())
                .imageUrls(request.getImageUrls())
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();
        return reviewRepository.save(review).getId() != null;
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(String productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId)
                .orElseThrow(()-> new NotFoundException("Reviews not found"));

        List<ReviewResponse> responses = new ArrayList<>();

        responses = reviews.stream().map(
                review -> {
                    User user = userRepository.findById(review.getUserId())
                            .orElseThrow(() -> new NotFoundException("User not found"));

                    Product product = productRepository.findById(review.getProductId())
                            .orElseThrow(() -> new NotFoundException("Product not found"));


                    Optional<ProductVariation> variation = product.getProductVariations()
                            .stream()
                            .filter(v -> v.getId().equals(review.getSelectedVariationId()))
                            .findFirst();


                    return ReviewResponse.builder()
                            .username(user.getUsername())
                            .rating(review.getRating())
                            .imageUrls(review.getImageUrls())
                            .comment(review.getComment())
                            .createdAt(review.getCreatedAt())
                            .color(variation.get().getColor())
                            .size(variation.get().getSize())
                            .capacity(variation.get().getCapacity())
                            .build();
                }

        ).collect(Collectors.toList());

        return responses;
    }

    @Override
    public List<ReviewResponse> getReviewsByUserId(String userId) {
        List<Review> reviews = reviewRepository.findAllByUserId(userId)
                .orElseThrow(()-> new NotFoundException("Reviews not found"));

        List<ReviewResponse> responses = new ArrayList<>();

        responses = reviews.stream().map(
                review -> {
                    User user = userRepository.findById(review.getUserId())
                            .orElseThrow(() -> new NotFoundException("User not found"));

                    Product product = productRepository.findById(review.getProductId())
                            .orElseThrow(() -> new NotFoundException("Product not found"));


                    Optional<ProductVariation> variation = product.getProductVariations()
                            .stream()
                            .filter(v -> v.getId().equals(review.getSelectedVariationId()))
                            .findFirst();


                    return ReviewResponse.builder()
                            .username(user.getUsername())
                            .rating(review.getRating())
                            .imageUrls(review.getImageUrls())
                            .productImage(product.getImages().get(0).getUrlImage())
                            .productName(product.getName())
                            .comment(review.getComment())
                            .createdAt(review.getCreatedAt())
                            .color(variation.get().getColor())
                            .size(variation.get().getSize())
                            .capacity(variation.get().getCapacity())
                            .build();
                }

        ).collect(Collectors.toList());

        return responses;
    }


}
