package com.shop.ecommerce.services;

import com.shop.ecommerce.models.OrderProduct;
import com.shop.ecommerce.responses.PaymentIntentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.util.List;

public interface PaymentService {
    public PaymentIntentResponse createOrUpdatePaymentIntent(List<OrderProduct> items, String paymentIntentId, int totalPrice) throws StripeException;
}
