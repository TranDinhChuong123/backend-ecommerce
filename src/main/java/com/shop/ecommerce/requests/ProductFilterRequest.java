package com.shop.ecommerce.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int limit = 30;
    private String sort ;
    @Builder.Default
    private String sortDirection = "asc";
    private boolean allProduct;
    private ProductFilter productFilter;
    private List<String> selectFields;
    private List<String> unSelectFields;
}
