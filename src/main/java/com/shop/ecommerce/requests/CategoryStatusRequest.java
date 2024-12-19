package com.shop.ecommerce.requests;

import com.shop.ecommerce.models.enums.CategoryStatus;
import lombok.Data;

@Data
public class CategoryStatusRequest {
    private CategoryStatus status;
}
