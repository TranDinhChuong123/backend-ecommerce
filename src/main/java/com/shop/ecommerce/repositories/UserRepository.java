package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String email);

    boolean existsByEmail(String email);

}