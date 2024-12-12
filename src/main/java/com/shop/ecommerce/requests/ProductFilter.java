package com.shop.ecommerce.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private String name;
    private String brand;
    private String categorySlug;
    private String price;
    private int discountPercent;
    private boolean totalSold;
}
