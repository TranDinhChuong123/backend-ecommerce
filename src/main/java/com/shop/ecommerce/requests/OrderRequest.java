package com.shop.ecommerce.requests;


import com.shop.ecommerce.models.Address;
import com.shop.ecommerce.models.OrderProduct;
import com.shop.ecommerce.models.Payment;
import com.shop.ecommerce.models.enums.OrderStatus;
import com.shop.ecommerce.models.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @Min(value = 0, message = "Shipping fee must be positive")
    private int feeShip;

    @Min(value = 0, message = "Total price must be positive")
    private int totalPrice;

    @NotEmpty(message = "Payment  cannot be empty")
    private Payment payment;

    @NotEmpty(message = "Product list cannot be empty")
    private List<@Valid OrderProduct> orderProducts;

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;

    private String note;

    private String paymentIntentId;
}
