package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.PaymentMethod;
import com.shop.ecommerce.models.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Payment {
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
//    private String transactionId; // Mã giao dịch từ hệ thống thanh toán
}
