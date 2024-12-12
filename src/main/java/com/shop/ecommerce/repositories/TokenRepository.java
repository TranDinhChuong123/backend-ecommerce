package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.Token;
import com.shop.ecommerce.models.enums.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
    Optional<Token> findByAccessToken(String accessToken);
    @Query("{ 'user_id': ?0, 'expired': false, 'revoked': false }")
    List<Token> findAllValidTokenByUser(String userId);
}
