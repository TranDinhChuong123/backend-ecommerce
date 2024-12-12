package com.shop.ecommerce.repositories;


import com.shop.ecommerce.models.Category;
import com.shop.ecommerce.models.enums.CategoryStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    // Custom query methods (if needed)
    Category findBySlug(String slug);
    List<Category> findByStatus(CategoryStatus status);

}