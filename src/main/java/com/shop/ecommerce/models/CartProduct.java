package com.shop.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {
    @Id
    private String id = UUID.randomUUID().toString();
    private String productId;
    private String selectedVariationId;
    private int buyQuantity;
    private boolean isChecked;

}
