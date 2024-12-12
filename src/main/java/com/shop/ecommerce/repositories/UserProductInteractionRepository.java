package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.UserProductInteraction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;
import java.util.List;

public interface UserProductInteractionRepository extends MongoRepository<UserProductInteraction,String> {
    List<UserProductInteraction> findByUserId(String userId);
    List<UserProductInteraction> findByProductId(String productId);
}
