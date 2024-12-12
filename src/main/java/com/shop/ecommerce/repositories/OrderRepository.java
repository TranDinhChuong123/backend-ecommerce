package com.shop.ecommerce.repositories;

import com.shop.ecommerce.models.Order;
import com.shop.ecommerce.models.Payment;
import com.shop.ecommerce.models.enums.OrderStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    @Query("{ 'status': { $in: ['COMPLETED'] } }")
    List<Order> findCompletedOrders();
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
    Optional<List<Order>> findByUserIdAndOrderStatus(String userId, OrderStatus orderStatus, Sort createdAt);

    Optional<Order> findByIdAndUserId(String orderId, String uerId);
    Optional<List<Order>> findByUserIdAndOrderStatusAndPayment(String userId, OrderStatus orderStatus, Payment payment, Sort createdAt);

    @Query("{ 'status': 'COMPLETED', 'created_at': { $gte: ?0, $lte: ?1 } }")
    List<Order> findCompletedOrdersWithinDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    Optional<List<Order>> findByOrderStatus(OrderStatus orderStatus);
}
