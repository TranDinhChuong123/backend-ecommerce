package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
}
