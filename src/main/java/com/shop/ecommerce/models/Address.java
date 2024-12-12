package com.shop.ecommerce.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    private String id ;
    private String name;
    private String phoneNumber;
    private String street;
    private boolean state ;
}
