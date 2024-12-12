package com.shop.ecommerce.requests;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRemoveRequest {
    @NotEmpty(message = "cartId cannot be empty")
    private String cartId;
    @NotEmpty(message = "productCartIds cannot be empty")
    private List<String> cartProductIds;
}
