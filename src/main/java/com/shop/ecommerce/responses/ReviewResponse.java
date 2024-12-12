package com.shop.ecommerce.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String username;
    private int rating;
    private List<String> imageUrls;
    private String productName;
    private String productImage;
    private String color;
    private String comment;
    private String capacity;
    private String size;
    private LocalDateTime createdAt;
}
