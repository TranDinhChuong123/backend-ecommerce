package com.shop.ecommerce.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUser {
    private String id;
    private String name;
    private String email;
    private String image;
}
