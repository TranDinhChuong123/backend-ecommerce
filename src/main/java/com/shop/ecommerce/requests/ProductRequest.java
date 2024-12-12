package com.shop.ecommerce.requests;


import com.shop.ecommerce.models.Image;
import com.shop.ecommerce.models.ProductVariation;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {



    @NotEmpty(message = "Name cannot be empty")
    private String name;

    private String brand;

    private String description;

    @NotEmpty(message = "Category cannot be empty")
    private String categorySlug;

    @NotEmpty(message = "Slug cannot be empty")
    private String slug;

    private List<Image> images;


    private List<ProductVariation> productVariations;




    // Add createdAt and updatedAt if needed
}
