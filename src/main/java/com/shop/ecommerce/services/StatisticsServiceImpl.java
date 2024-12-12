package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Inventory;
import com.shop.ecommerce.models.InventoryTransaction;
import com.shop.ecommerce.models.Order;
import com.shop.ecommerce.models.OrderProduct;
import com.shop.ecommerce.models.enums.OrderStatus;
import com.shop.ecommerce.repositories.*;
import com.shop.ecommerce.responses.OrderStatisticResponse;
import com.shop.ecommerce.responses.StatisticOrderByStatusRes;
import com.shop.ecommerce.responses.StatisticResponse;
import com.shop.ecommerce.responses.StatisticsResponse;
import com.shop.ecommerce.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final  UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final InventoryRepository inventoryRepository;
    private final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);



    @Override
    public List<OrderStatisticResponse> calculateStatisticsOrder() {
        List<Order> completedOrders = orderRepository.findCompletedOrders();

        List<OrderStatisticResponse> orderStatistics = new ArrayList<>();

        for (Order order : completedOrders) {
            double totalRevenue = 0;
            double totalProfit = 0;

            for (OrderProduct orderProduct : order.getOrderProducts()) {
                String variationId = orderProduct.getSelectedVariationId();
                int quantity = orderProduct.getBuyQuantity();
                double price = orderProduct.getPrice();
                double discount = (100.0 - orderProduct.getDiscountPercent()) / 100.0;

                logger.info("price = " + price + ", quantity = " + quantity + ", discount = " + discount);

                // Tính doanh thu
                totalRevenue += price * quantity * discount;

                // Lấy thông tin inventory để tính giá nhập trung bình
                Inventory inventory = inventoryRepository.findByVariationId(variationId)
                        .orElseThrow(() -> new RuntimeException("Inventory not found for variationId: " + variationId));

                double averageCost = inventory.getTotalCost() / inventory.getTotalQuantityIn();
                logger.info("averageCost = " + averageCost);

                // Tính lợi nhuận
                totalProfit += (price * discount - averageCost) * quantity;
            }

            OrderStatisticResponse orderStatisticResponse = new OrderStatisticResponse(
                    order.getId(),
                    order.getUserId(),
                    order.getCreatedAt(),
                    order.getDeliveryDate(),
                    totalRevenue,
                    totalProfit
            );

            orderStatistics.add(orderStatisticResponse);
        }

        return orderStatistics;
    }

    @Override
    public List<OrderStatisticResponse> calculateStatisticsOrderDate(LocalDateTime startDate, LocalDateTime endDate) {
        // Lọc các đơn hàng đã hoàn thành và nằm trong khoảng thời gian đã cho
        List<Order> completedOrders = orderRepository.findCompletedOrdersWithinDateRange(startDate, endDate);

        List<OrderStatisticResponse> orderStatistics = new ArrayList<>();

        for (Order order : completedOrders) {
            double totalRevenue = 0;
            double totalProfit = 0;

            for (OrderProduct orderProduct : order.getOrderProducts()) {
                String variationId = orderProduct.getSelectedVariationId();
                int quantity = orderProduct.getBuyQuantity();
                double price = orderProduct.getPrice();
                double discount = (100.0 - orderProduct.getDiscountPercent()) / 100.0;

                logger.info("price = " + price + ", quantity = " + quantity + ", discount = " + discount);

                // Tính doanh thu
                totalRevenue += price * quantity * discount;

                // Lấy thông tin inventory để tính giá nhập trung bình
                Inventory inventory = inventoryRepository.findByVariationId(variationId)
                        .orElseThrow(() -> new RuntimeException("Inventory not found for variationId: " + variationId));

                double averageCost = inventory.getTotalCost() / inventory.getTotalQuantityIn();
                logger.info("averageCost = " + averageCost);

                // Tính lợi nhuận
                totalProfit += (price * discount - averageCost) * quantity;
            }

            OrderStatisticResponse orderStatisticResponse = new OrderStatisticResponse(
                    order.getId(),
                    order.getUserId(),
                    order.getCreatedAt(),
                    order.getDeliveryDate(),
                    totalRevenue,
                    totalProfit
            );

            orderStatistics.add(orderStatisticResponse);
        }

        return orderStatistics;
    }



    public StatisticResponse calculateStatistics() {
        List<Order> completedOrders = orderRepository.findCompletedOrders();
        double totalRevenue = 0;
        double totalProfit = 0;
        for (Order order : completedOrders) {
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                String variationId = orderProduct.getSelectedVariationId();
                int quantity = orderProduct.getBuyQuantity();
                double price = orderProduct.getPrice();


                double  discount = (double) (100 - orderProduct.getDiscountPercent()) /100;

                // Tính doanh thu
                totalRevenue += price * quantity*discount;

                // Tính giá nhập trung bình
                Inventory inventory = inventoryRepository.findByVariationId(variationId).orElseThrow();;
                double averageCost = inventory.getTotalCost() / inventory.getTotalQuantityIn();

                // Tính lợi nhuận
                totalProfit += (price * discount - averageCost) * quantity;
            }
        }

        return StatisticResponse.builder()
                .totalRevenue(totalRevenue)
                .totalProfit(totalProfit)
                .build();
    }



    @Override
    public StatisticsResponse getStatistics() {
        double totalRevenue = orderRepository.findAll().stream()
                .mapToDouble(Order::getTotalPrice).sum();
        return StatisticsResponse.builder()
                .totalCustomer(userRepository.count())
                .totalProduct(productRepository.count())
                .totalOrder(orderRepository.count())
                .totalRevenue(calculateStatistics().getTotalRevenue())
                .build();
    }

    @Override
    public StatisticOrderByStatusRes getOrderStatisticsByStatus() {
        return StatisticOrderByStatusRes.builder()
                .totalOrderCanceled(orderRepository.findByOrderStatus(OrderStatus.CANCELED)
                        .orElseThrow().size())
                .totalOrderPending(orderRepository.findByOrderStatus(OrderStatus.PENDING)
                        .orElseThrow().size())
                .totalOrderCompleted(orderRepository.findByOrderStatus(OrderStatus.COMPLETED)
                        .orElseThrow().size())
                .totalOrderShipping(orderRepository.findByOrderStatus(OrderStatus.SHIPPING)
                        .orElseThrow().size())
                .build();
    }
}
