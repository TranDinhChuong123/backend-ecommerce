package com.shop.ecommerce.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticOrderByStatusRes {
    private int totalOrderPending;
    private int totalOrderCompleted;
    private int totalOrderCanceled;
    private int totalOrderShipping;
}
