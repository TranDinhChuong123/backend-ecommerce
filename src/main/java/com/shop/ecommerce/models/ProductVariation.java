package com.shop.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.shop.ecommerce.models.enums.VariationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "product_variations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariation {

    @Field(name = "id")
    private String id ;

    @Field(name = "product_id")
    private String productId;

    @Field(name = "url_image")
    private String urlImage;

    @Field(name = "color")
    private String color;

    @Field(name = "capacity")
    private String capacity;

    @Field(name = "size")
    private String size;

    @Field(name = "status")
    private VariationStatus status;

    @Field(name = "price")
    private int price;

    @Field(name = "discount_percent")
    private int discountPercent;

    @Field(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

}

