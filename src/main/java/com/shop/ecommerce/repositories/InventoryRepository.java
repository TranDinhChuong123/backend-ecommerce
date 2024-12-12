package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.Inventory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {


    @Query("{ 'variationId': ?0 }")
    List<Inventory> findInventoriesByVariationId(String variationId);

    Optional<List<Inventory>> findByProductId(ObjectId productId);

    Optional<Inventory> findByVariationId(String variationId);
}
