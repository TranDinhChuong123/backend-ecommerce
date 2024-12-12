package com.shop.ecommerce.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private String id;
    private String variationId;
    private String name;
    private String urlImage;
    private String color;
    private String capacity;
    private String size;
    private int price;
    private int discountPercent;
    private int quantity;
}
