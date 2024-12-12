package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.Product;

import com.shop.ecommerce.models.enums.ProductStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
//    @Query(value = "{ $text: { $search: ?0 }}", fields = "{ score: { $meta: 'textScore' } }")
//    List<Product> searchProductsByKeyword(String keyword);

    List<Product> findByOrderByTotalSoldDesc(Pageable pageable);
    Optional<List<Product>> findProductsByStatus(ProductStatus status);

}



