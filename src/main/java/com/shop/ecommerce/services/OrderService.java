package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Order;
import com.shop.ecommerce.models.enums.OrderStatus;
import com.shop.ecommerce.requests.CancelOrderRequest;
import com.shop.ecommerce.requests.OrderRequest;
import com.shop.ecommerce.requests.ReOrderRequest;
import com.shop.ecommerce.responses.RevenueDayResponse;
import com.shop.ecommerce.responses.RevenueMonthResponse;
import java.util.List;

public interface OrderService {
    boolean createNewOrder(OrderRequest request);

    boolean cancelOrder(CancelOrderRequest request);

    boolean reOrder(ReOrderRequest request);

    List<RevenueMonthResponse> getRevenueByMonth(int year);

    List<RevenueDayResponse> getRevenueByDayInMonth(int month, int year);


    Order getOrderById(String orderId);

    List<Order> getAllOrders();
    List<Order> updateStatus(OrderStatus status);
    boolean confirmOrder(String orderId, String userId);
    boolean completeOrder(String orderId);
}
