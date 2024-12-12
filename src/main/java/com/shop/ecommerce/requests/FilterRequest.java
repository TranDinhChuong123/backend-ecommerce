package com.shop.ecommerce.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private int page = 1;
    private int limit = 100;
    private String sort;
    private String sortDirection;
    private List<String> selectFields;
    private List<String> unSelectFields;
}
