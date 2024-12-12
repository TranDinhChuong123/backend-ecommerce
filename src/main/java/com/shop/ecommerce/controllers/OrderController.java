package com.shop.ecommerce.controllers;

import com.shop.ecommerce.models.Order;
import com.shop.ecommerce.models.enums.OrderStatus;
import com.shop.ecommerce.requests.CancelOrderRequest;
import com.shop.ecommerce.requests.OrderRequest;
import com.shop.ecommerce.requests.ReOrderRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.responses.RevenueDayResponse;
import com.shop.ecommerce.responses.RevenueMonthResponse;
import com.shop.ecommerce.services.OrderService;

import com.stripe.model.tax.Registration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @PostMapping("/create")
    public ApiResponse<Boolean> createNewOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderRequest request
    ) {
        logger.info(" request {}", request);
        request.setUserId(userDetails.getUsername());
        return new ApiResponse<>(
                200,
                "Order created successfully",
                orderService.createNewOrder(request)
        );
    }

    @PostMapping("/canceled")
    public ApiResponse<Boolean> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CancelOrderRequest request
    ) {
        request.setUserId(userDetails.getUsername());
        return new ApiResponse<>(
                200,
                "Order cancelled successfully",
                orderService.cancelOrder(request)
        );
    }

    @PostMapping("/re-order")
    public ApiResponse<Boolean> reOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ReOrderRequest request
    ) {
        request.setUserId(userDetails.getUsername());
        return new ApiResponse<>(
                200,
                "reOrder successfully",
                orderService.reOrder(request)
        );
    }





    @GetMapping("/{orderId}")
    public ApiResponse<Order> getOrderByOrderId(@PathVariable String orderId){

        return new ApiResponse<>(
                "Order found successfully",
                orderService.getOrderById(orderId)
        );
    }

    @GetMapping("/revenue/{year}")
    public ApiResponse<List<RevenueMonthResponse>> getRevenueByMonth(@PathVariable int year){

        return new ApiResponse<>(
                "GetRevenueByMonth successfully",
                orderService.getRevenueByMonth(year)
        );
    }

    @GetMapping("/revenue/{month}/{year}")
    public ApiResponse<List<RevenueDayResponse>> getRevenueByDayInMonth(
            @PathVariable int month,
            @PathVariable int year
    ){
        return new ApiResponse<>(
                "GetRevenueByDayInMonth successfully",
                orderService.getRevenueByDayInMonth(month,year)
        );
    }
//    code ong tri
    @GetMapping("")
    public ApiResponse<List<Order>> getAllOrders() {
        return new ApiResponse<>(
                "Orders retrieved successfully",
                orderService.getAllOrders()
        );
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<Order>> updateStatus(@PathVariable String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return new ApiResponse<>(
                "Orders retrieved successfully",
                orderService.updateStatus(orderStatus)
        );
    }
    @PutMapping("/confirm/{orderId}")
    public ResponseEntity<ApiResponse<Boolean>> confirmOrder(
            @PathVariable String orderId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            boolean isConfirmed = orderService.confirmOrder(orderId,userDetails.getUsername());
            if (isConfirmed) {
                return ResponseEntity.ok(new ApiResponse<>(200, "Order confirmed successfully", true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "Order not found", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error confirming order: " + e.getMessage(), false));
        }
    }


    @PutMapping("/complete/{orderId}")
    public ApiResponse<Boolean> completeOrder(@PathVariable String orderId) {
        return new ApiResponse<>(
                200,
                "Order completed successfully",
                orderService.completeOrder(orderId));
    }












}
