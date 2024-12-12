package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.CategoryStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Category {

    @Id
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "slug")
    private String slug;

    @Field(name = "image_url")
    private String imageUrl;

    private CategoryStatus status;


    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "updated_by")
    private LocalDateTime updatedBy;

    @Field(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
