package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<Supplier, String> {
}
