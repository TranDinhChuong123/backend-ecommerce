package com.shop.ecommerce.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticResponse {
    private double totalRevenue; // Tổng doanh thu
    private double totalProfit;  // Tổng lợi nhuận
}