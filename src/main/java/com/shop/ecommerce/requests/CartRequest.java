package com.shop.ecommerce.requests;

import com.shop.ecommerce.models.CartProduct;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CartRequest {


    @NotEmpty(message = "userId cannot be empty")
    private String userId;

    @NotEmpty(message = "cartProduct cannot be empty")
    private CartProduct cartProduct;
}
