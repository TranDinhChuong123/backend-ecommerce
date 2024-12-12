package com.shop.ecommerce.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    @Id
    private String id;
    @NotEmpty(message = "name cannot be empty")
    private String name;
    @NotEmpty(message = "phoneNumber cannot be empty")
    private String phoneNumber;
    @NotBlank(message = "street is required")
    private String street;
}