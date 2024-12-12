package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.Cart;
import com.shop.ecommerce.models.User;
import com.shop.ecommerce.models.enums.CartState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository  extends MongoRepository<Cart, String> {
    Optional<Cart> findCartByUserId(String userId);
    Optional<Cart> findCartByUserIdAndCartState(String userId, CartState state);
}
