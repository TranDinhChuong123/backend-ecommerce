package com.shop.ecommerce.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.ecommerce.models.Image;
import com.shop.ecommerce.models.ProductVariation;
import com.shop.ecommerce.models.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartProductResponse  {
    @Id
    private String id;
    private String productId;
    private String selectedVariationId;
    @JsonProperty("isChecked")
    private boolean isChecked;
    private String name;
    private String urlImage;
    private String color;
    private String capacity;
    private String slug;
    private String size;
    private int price;
    private int discountPercent;
    private int quantity;
    private int buyQuantity;
}
