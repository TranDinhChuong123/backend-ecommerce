package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.Order;
import com.shop.ecommerce.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<List<Review>> findAllByProductId(String productId);

    Optional<List<Review>> findAllByUserId(String productId);



}
