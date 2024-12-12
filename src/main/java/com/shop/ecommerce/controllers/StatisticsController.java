package com.shop.ecommerce.controllers;

import com.shop.ecommerce.responses.*;
import com.shop.ecommerce.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatisticsController {
    private final StatisticsService statisticsService;


    @GetMapping("/orders-date")
    public ApiResponse<List<OrderStatisticResponse>> calculateStatisticsOrderDate(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr
    ) {
        // Thêm thời gian mặc định nếu chỉ có ngày
        String startDateTimeStr = startDateStr.length() == 10 ? startDateStr + "T00:00:00" : startDateStr;
        String endDateTimeStr = endDateStr.length() == 10 ? endDateStr + "T23:59:59" : endDateStr;

        // Chuyển đổi sang LocalDateTime
        LocalDateTime startDate = LocalDateTime.parse(startDateTimeStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateTimeStr);

        // Gọi phương thức từ service để lấy dữ liệu thống kê
        List<OrderStatisticResponse> result = statisticsService.calculateStatisticsOrderDate(startDate, endDate);

        return new ApiResponse<>(200, "GetStatistics successfully", result);
    }



    @GetMapping("/revenue-profit-orders")
    public ApiResponse<List<OrderStatisticResponse>> calculateStatisticsOrder() {
        List<OrderStatisticResponse> statistics = statisticsService.calculateStatisticsOrder();
        return new ApiResponse<>(
                200,
                "GetStatistics successfully",
                statistics
        );
    }


    @GetMapping("/revenue-profit")
    public ApiResponse<StatisticResponse> getRevenueAndProfit() {
        StatisticResponse response = statisticsService.calculateStatistics();
        return new ApiResponse<>(
                200,
                "GetStatistics successfully",
                response
        );
    }
    @GetMapping("")
    public ApiResponse<StatisticsResponse> getStatistics() {
        return new ApiResponse<>(
                200,
                "GetStatistics successfully",
                statisticsService.getStatistics()
        );
    }
    @GetMapping("/orders-status")
    public ApiResponse<StatisticOrderByStatusRes> getOrderStatisticsByStatus() {
        return new ApiResponse<>(
                200,
                "GetStatistics successfully",
                statisticsService.getOrderStatisticsByStatus()
        );
    }


}
