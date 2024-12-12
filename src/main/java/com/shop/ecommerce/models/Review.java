package com.shop.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    private String id;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "order_id")
    private String orderId;

    @Field(name = "product_id")
    private String productId;

    @Field(name = "selected_variation_id")
    private String selectedVariationId;

    @Field(name = "rating")
    private int rating;

    @Field(name = "comment")
    private String comment;

    @Field(name = "image_urls")
    private List<String> imageUrls;

    @Field(name = "createdAt")
    private LocalDateTime createdAt;
}
