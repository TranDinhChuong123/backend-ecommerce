package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.OrderStatus;
import com.shop.ecommerce.models.enums.PaymentMethod;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Document(collection = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "fee_ship")
    private int feeShip;

    @Field(name = "total_price")
    private int totalPrice;

    @Field(name = "payment")
    private Payment payment;

    @Field(name = "note")
    private String note;

    @Field(name = "address")
    private Address address;

    @Field(name = "products")
    private List<OrderProduct> orderProducts;

    @Field(name = "status")
    private OrderStatus orderStatus;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "cancel_reason")
    private String cancelReason;

    @Field(name = "cancel_date")
    private LocalDateTime cancelDate;

    @Field(name = "delivery_date")
    private LocalDateTime deliveryDate;


}
