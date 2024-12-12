package com.shop.ecommerce.responses;

import com.shop.ecommerce.models.Image;
import com.shop.ecommerce.models.ProductVariation;
import com.shop.ecommerce.models.enums.ProductStatus;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

public class ProductStoredResponse {
    @Id
    private String id ;
    @Indexed
    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "brand")
    private String brand;

    @Field(name = "category")
    private String category;

    private String supplierId;

    @Field(name = "discount_percent")
    private int discountPercent;

    @Field(name = "total_sold")
    private int totalSold;

    @Field(name = "slug")
    private String slug;

    @Field(name = "images")
    private List<Image> images;

    @Field(name = "product_variations")
    private List<ProductVariation> productVariations;

    @Field(name = "status")
    private ProductStatus status;

    @Field(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;
}
