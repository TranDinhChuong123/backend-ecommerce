package com.shop.ecommerce.services;

import com.shop.ecommerce.responses.OrderStatisticResponse;
import com.shop.ecommerce.responses.StatisticOrderByStatusRes;
import com.shop.ecommerce.responses.StatisticResponse;
import com.shop.ecommerce.responses.StatisticsResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {

    List<OrderStatisticResponse> calculateStatisticsOrderDate(LocalDateTime startDate, LocalDateTime endDate);

    List<OrderStatisticResponse> calculateStatisticsOrder();
    StatisticResponse calculateStatistics();
    StatisticsResponse getStatistics();
    StatisticOrderByStatusRes getOrderStatisticsByStatus();
}
