package com.shop.ecommerce.responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class SKUDetailsResponse {
    private String id;
    private String productId;
    private String productName;
    private String urlImage;
    private String color;
    private String capacity;
    private String size;
    private int price;
    private int discountPercent;

}
