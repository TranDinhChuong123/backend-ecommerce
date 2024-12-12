package com.shop.ecommerce.repositories;



import com.shop.ecommerce.models.SKUDetails;
import com.shop.ecommerce.models.enums.ProductAttributeStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SKUDetailsRepository extends MongoRepository<SKUDetails, String> {
    List<SKUDetails> findByStatus(ProductAttributeStatus status);
}
