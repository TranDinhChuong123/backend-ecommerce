package com.shop.ecommerce.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderStatisticResponse {
    private String orderId;
    private String userId;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private double totalRevenue;
    private double totalProfit;

}
