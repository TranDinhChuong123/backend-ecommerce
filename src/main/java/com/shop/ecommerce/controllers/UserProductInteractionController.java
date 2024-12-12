package com.shop.ecommerce.controllers;


import com.shop.ecommerce.models.UserProductInteraction;
import com.shop.ecommerce.repositories.ProductRepository;
import com.shop.ecommerce.requests.ProductRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.services.ProductService;
import com.shop.ecommerce.services.UserProductInteractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-product-interaction")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserProductInteractionController {
    private final UserProductInteractionService service;

    @PostMapping("/create")
    public ApiResponse<Boolean> createUserProductInteraction(@RequestBody @Valid UserProductInteraction userProductInteraction) {
        return new ApiResponse<>(
                201,
                "Created Product successfuly",
                service.createUserProductInteraction(userProductInteraction)
        );
    }
}
