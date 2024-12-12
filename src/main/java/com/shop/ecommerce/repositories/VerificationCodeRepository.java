package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.VerificationCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface  VerificationCodeRepository extends MongoRepository<VerificationCode, String> {
    Optional<VerificationCode> findByEmail(String email);
    Optional<List<VerificationCode>> findAllByEmail(String email);
}
