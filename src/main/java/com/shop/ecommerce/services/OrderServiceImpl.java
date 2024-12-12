package com.shop.ecommerce.services;

import com.shop.ecommerce.exception.NotFoundException;
import com.shop.ecommerce.models.*;
import com.shop.ecommerce.models.enums.*;
import com.shop.ecommerce.repositories.*;
import com.shop.ecommerce.requests.CancelOrderRequest;
import com.shop.ecommerce.requests.OrderRequest;
import com.shop.ecommerce.requests.ReOrderRequest;
import com.shop.ecommerce.responses.RevenueDayResponse;
import com.shop.ecommerce.responses.RevenueMonthResponse;
import com.shop.ecommerce.responses.StatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserProductInteractionRepository userProductInteractionRepository;
    private final InventoryRepository inventoryRepository;
    private final MongoTemplate mongoTemplate;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    @Override
    public boolean createNewOrder(OrderRequest request) {
        User user = userRepository.findUserByUsername(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Address address = user.getAddresses().stream()
                .filter(Address::isState)
                .findFirst()
                .orElse(null);

        Cart cart = cartRepository.findCartByUserIdAndCartState(request.getUserId(), CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart active not found !"));
        cart.getCartProducts().removeIf(cartProduct ->
                request.getOrderProducts().stream()
                        .anyMatch(orderProduct -> cartProduct.getProductId().equals(orderProduct.getProductId())
                                && cartProduct.getSelectedVariationId().equals(orderProduct.getSelectedVariationId()))
        );
        cartRepository.save(cart);


        request.getOrderProducts().stream().map(orderProduct -> {
            Product product = productRepository.findById(orderProduct.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
//            product.getProductVariations().forEach(variation -> {
//                if (variation.getId().equals(orderProduct.getSelectedVariationId())) {
//                    variation.setSoldQuantity(variation.getSoldQuantity() + orderProduct.getBuyQuantity());
//                }
//            });
//            product.setTotalSold(product.getTotalSold()+ orderProduct.getBuyQuantity());
//            productRepository.save(product);

//            Inventory inventory = inventoryRepository
//                    .findByVariationId(orderProduct.getSelectedVariationId())
//                    .orElseThrow(()-> new RuntimeException("Could not find"));
//
//            inventory.setTotalQuantityIn(inventory.getTotalQuantityIn() - orderProduct.getBuyQuantity());

//            inventoryRepository.save(inventory);
            return orderProduct;
        }).toList();



        if (request.getPayment().getPaymentMethod() == PaymentMethod.WALLET
                && request.getPayment().getPaymentStatus() == PaymentStatus.COMPLETED) {
            user.setWallet(user.getWallet() - request.getTotalPrice());
            userRepository.save(user);
        }


        Order order = Order.builder()
                .note(request.getNote())
                .userId(request.getUserId())
                .address(address)
                .feeShip(request.getFeeShip())
                .payment(request.getPayment())
                .orderProducts(request.getOrderProducts())
                .totalPrice(request.getTotalPrice())
                .orderStatus(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        Order orderCreated = orderRepository.save(order);
        if (orderCreated.getId() == null) throw new RuntimeException("Order creation failed ! " + order);

        List<UserProductInteraction> interactions = request.getOrderProducts()
                .stream()
                .filter(orderProduct -> orderProduct.getProductId() != null && request.getUserId() != null) // Kiểm tra userId và productId không null
                .map(orderProduct -> UserProductInteraction.builder()
                        .userId(request.getUserId())
                        .productId(orderProduct.getProductId())
                        .interactionType(InteractionType.PURCHASED)
                        .interactionTime(LocalDateTime.now())
                        .build()
                ).collect(Collectors.toList());


        userProductInteractionRepository.saveAll(interactions);

        return true;
    }

    @Override
    public boolean cancelOrder(CancelOrderRequest request) {
        Order order = orderRepository.findByIdAndUserId(request.getOrderId(), request.getUserId())
                .orElseThrow(() -> new NotFoundException("Order not found"));


        // Kiểm tra trạng thái đơn hàng, chỉ có thể hủy nếu đơn hàng chưa bị hủy hoặc đã hoàn thành
        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Order is already canceled");
        }
        // Cập nhật trạng thái đơn hàng thành CANCELED
        order.setOrderStatus(OrderStatus.CANCELED);
        order.setCancelReason(request.getCancelReason());
        order.setCancelDate(LocalDateTime.now());

        Cart cart = cartRepository.findCartByUserIdAndCartState(request.getUserId(), CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart active not found !"));

        // Cập nhật lại số lượng sản phẩm đã mua
        order.getOrderProducts().forEach(orderProduct -> {
//            Product product = productRepository.findById(orderProduct.getProductId())
//                    .orElseThrow(() -> new NotFoundException("Product not found"));
//
////            product.getProductVariations().forEach(variation -> {
////                if (variation.getId().equals(orderProduct.getSelectedVariationId())) {
////                    variation.setSoldQuantity(variation.getSoldQuantity() - orderProduct.getBuyQuantity());
////                }
////            });
//
////            product.setTotalSold(product.getTotalSold() - orderProduct.getBuyQuantity());
//            productRepository.save(product);



//            Inventory inventory = inventoryRepository
//                    .findByVariationId(orderProduct.getSelectedVariationId())
//                    .orElseThrow(()-> new RuntimeException("Could not find"));
//            inventory.setQuantityInStock(inventory.getQuantityInStock() + orderProduct.getBuyQuantity());
//
//            InventoryTransaction transaction = InventoryTransaction.builder()
//                    .type(TransactionType.CANCEL)
//                    .quantity(orderProduct.getBuyQuantity())
//                    .orderId(order.getId())
//                    .transactionDate(LocalDateTime.now())
//                    .inventoryId(inventory.getId())
//                    .build();
//            inventoryTransactionRepository.save(transaction);
//
//            inventoryRepository.save(inventory);

            cart.getCartProducts().add(CartProduct.builder()
                    .id(orderProduct.getId())
                    .buyQuantity(orderProduct.getBuyQuantity())
                    .productId(orderProduct.getProductId())
                    .selectedVariationId(orderProduct.getSelectedVariationId())
                    .isChecked(true)
                    .build());

        });

        cartRepository.save(cart);
        orderRepository.save(order);

        if (order.getPayment().getPaymentStatus() == PaymentStatus.COMPLETED) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found !!!"));
            user.setWallet(order.getTotalPrice() + user.getWallet());
            userRepository.save(user);
        }


        return true;
    }

    @Override
    public boolean reOrder(ReOrderRequest request) {
        // Lấy đơn hàng từ cơ sở dữ liệu
        Order order = orderRepository.findByIdAndUserId(request.getOrderId(), request.getUserId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        Cart cart = cartRepository.findCartByUserIdAndCartState(request.getUserId(), CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart active not found !"));

        cart.getCartProducts().forEach(cartProduct -> cartProduct.setChecked(false));

        order.getOrderProducts().forEach(orderProduct -> {
            Product product = productRepository.findById(orderProduct.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));


            Optional<CartProduct> optionalCartProduct = cart.getCartProducts().stream()
                    .filter(cartProduct ->
                            cartProduct.getProductId().equals(orderProduct.getProductId())
                                    && cartProduct.getSelectedVariationId().equals(orderProduct.getSelectedVariationId())
                    ).findFirst();
           if(optionalCartProduct.isPresent()){
               optionalCartProduct.ifPresent(cartProduct -> {
                   cartProduct.setChecked(true);
               });
           }else{
               // Thêm sản phẩm mới vào giỏ hàng
               cart.getCartProducts().add(CartProduct.builder()
                       .id(orderProduct.getId())
                       .buyQuantity(orderProduct.getBuyQuantity())
                       .productId(orderProduct.getProductId())
                       .selectedVariationId(orderProduct.getSelectedVariationId())
                       .isChecked(true) // Đánh dấu là đã chọn khi thêm mới
                       .build());
           }
        });
        cartRepository.save(cart);
        return true;
    }






    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }



    // Phương thức tính doanh thu theo tháng
    @Override
    public List<RevenueMonthResponse> getRevenueByMonth(int year) {
        Aggregation aggregation = Aggregation.newAggregation(
                // Lọc các đơn hàng trong khoảng thời gian của năm
                Aggregation.match(Criteria.where("created_at")
                        .gte(LocalDateTime.of(year, 1, 1, 0, 0))
                        .lt(LocalDateTime.of(year + 1, 1, 1, 0, 0))
                        .and("status").is("COMPLETED")),

                // Thêm một trường mới "month" từ createdAt
                Aggregation.project()
                        .andExpression("month(created_at)").as("month")
                        .and("total_price").as("total_price"),

                // Nhóm theo tháng và tính tổng doanh thu
                Aggregation.group("month")
                        .sum("total_price").as("totalRevenue"),

                // Chuyển đổi lại các trường mong muốn
                Aggregation.project("totalRevenue")
                        .and("_id").as("month")
                        .andExclude("_id")
        );

        // Thực hiện aggregation
        AggregationResults<RevenueMonthResponse> results = mongoTemplate.aggregate(aggregation, "orders", RevenueMonthResponse.class);

        return results.getMappedResults();
    }

    @Override
    public List<RevenueDayResponse> getRevenueByDayInMonth(int month, int year) {
        // Tạo thời gian bắt đầu và kết thúc của tháng
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        Aggregation aggregation = Aggregation.newAggregation(
                // Lọc các đơn hàng trong tháng đã cho
                Aggregation.match(Criteria.where("created_at")
                        .gte(startOfMonth)
                        .lt(endOfMonth)
                        .and("status").is("COMPLETED")),

                // Thêm một trường mới "day" từ createdAt
                Aggregation.project()
                        .andExpression("dayOfMonth(created_at)").as("day")
                        .and("total_price").as("total_price"),

                // Nhóm theo ngày và tính tổng doanh thu
                Aggregation.group("day")
                        .sum("total_price").as("totalRevenue"),

                // Chuyển đổi lại các trường mong muốn
                Aggregation.project("totalRevenue")
                        .and("_id").as("day")
                        .andExclude("_id")
        );

        // Thực hiện aggregation
        AggregationResults<RevenueDayResponse> results = mongoTemplate.aggregate(aggregation, "orders", RevenueDayResponse.class);

        return results.getMappedResults();
    }


    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> updateStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status)
                .orElseThrow(() -> new NotFoundException("No orders found with status: " + status));
    }
    @Override
    public boolean confirmOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));


        if (order.getOrderStatus() == OrderStatus.PENDING) {
            order.setOrderStatus(OrderStatus.SHIPPING);
            orderRepository.save(order);


            List<Inventory> inventories = order.getOrderProducts().stream().map(orderProduct -> {
                Inventory inventory = inventoryRepository.findByVariationId(orderProduct.getSelectedVariationId())
                        .orElseThrow(() -> new NotFoundException("Inventory not found for variation: " + orderProduct.getSelectedVariationId()));

                inventory.setQuantityInStock(inventory.getQuantityInStock() - orderProduct.getBuyQuantity());

                inventoryTransactionRepository.save(InventoryTransaction.builder()
                                .inventoryId(inventory.getId())
                                .orderId(orderId)
                                .quantity(orderProduct.getBuyQuantity())
                                .type(TransactionType.OUT)
                                .transactionDate(LocalDateTime.now())
                                .performedBy(userId)
                        .build()
                );
                Product product = productRepository.findById(orderProduct.getProductId())
                        .orElseThrow();
                product.setTotalSold(product.getTotalSold() + orderProduct.getBuyQuantity());
                productRepository.save(product);


                return inventory;
            }).toList();
            inventoryRepository.saveAll(inventories);

            return true;
        }
        return false;
    }

    @Override
    public boolean completeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (order.getOrderStatus() == OrderStatus.SHIPPING) {
            order.setOrderStatus(OrderStatus.COMPLETED);
            order.setDeliveryDate(LocalDateTime.now());
            order.getPayment().setPaymentStatus(PaymentStatus.COMPLETED);
            orderRepository.save(order);
            return true;
        }
        return false;
    }


}
