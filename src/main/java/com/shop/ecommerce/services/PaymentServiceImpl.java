package com.shop.ecommerce.services;

import com.shop.ecommerce.models.OrderProduct;
import com.shop.ecommerce.responses.PaymentIntentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService{
    private final OrderService orderService;

    @Value("${stripe.secret.key}")
    private  String stripeSecretKey;

    public PaymentServiceImpl(OrderService orderService, @Value("${stripe.secret.key}") String stripeSecretKey) {
        this.orderService = orderService;
        this.stripeSecretKey = stripeSecretKey;
        Stripe.apiKey = stripeSecretKey; // Khởi tạo API key
    }

//    public int calculateOrderAmount(List<OrderProduct> items) {
//        return items.stream()
//                .mapToInt(item -> item.getPrice() * item.getBuyQuantity())
//                .sum();
//    }


    @Override
    public PaymentIntentResponse createOrUpdatePaymentIntent(List<OrderProduct> items, String paymentIntentId, int totalPrice) throws StripeException {


        if (paymentIntentId != null && !paymentIntentId.isEmpty()) {

            PaymentIntent existingIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntentUpdateParams updateParams = PaymentIntentUpdateParams.builder()
                    .setAmount((long) totalPrice )
                    .build();
             existingIntent.update(updateParams);

             return  PaymentIntentResponse.builder()
                     .id(existingIntent.getId())
                     .clientSecret(existingIntent.getClientSecret())
                     .status(existingIntent.getStatus())
                     .build();
        } else {
            // Tạo Payment Intent mới
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) totalPrice )
                    .setCurrency("vnd")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
                    .build();
            PaymentIntent newIntent = PaymentIntent.create(params);

            return PaymentIntentResponse.builder()
                    .id(newIntent.getId())
                    .clientSecret(newIntent.getClientSecret())
                    .status(newIntent.getStatus())
                    .build();
        }

    }


}
