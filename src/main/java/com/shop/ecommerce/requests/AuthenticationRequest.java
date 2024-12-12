package com.shop.ecommerce.requests;

import com.shop.ecommerce.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String name;
    private String email;
    private String image;
    private String authProvider;
    private String password;
    private String phoneNumber;
    private Role role;
}
