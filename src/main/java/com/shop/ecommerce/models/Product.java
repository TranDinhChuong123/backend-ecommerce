package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.ProductStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product {
    @Id
    private String id ;
    @Indexed
    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "brand")
    private String brand;

    @Field(name = "category_slug")
    private String categorySlug;

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
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();


}
