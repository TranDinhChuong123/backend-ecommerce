package com.shop.ecommerce.controllers;

import com.shop.ecommerce.requests.OrderRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.responses.PaymentIntentResponse;
import com.shop.ecommerce.services.OrderService;
import com.shop.ecommerce.services.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentController {

    private final PaymentService paymentService;
//    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @PostMapping("/create-payment-intent")
    public ApiResponse<PaymentIntentResponse> createPaymentIntent(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderRequest request
    ) throws StripeException {
        logger.info(" request {}", request);
            PaymentIntentResponse paymentIntent = paymentService.createOrUpdatePaymentIntent(
                    request.getOrderProducts(),
                    request.getPaymentIntentId(),
                    request.getTotalPrice()
            );
            request.setUserId(userDetails.getUsername());
//            orderService.createNewOrder(request);
            return new ApiResponse<>(
                    200,
                    "Payment intent created successfully",
                    paymentIntent
            );
    }

}
